package ict.db;

import ict.bean.Appointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public AppointmentDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) {}
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public synchronized Appointment createAppointment(int patientId, int serviceId, LocalDateTime slotTime) {
        if (hasDoubleBooking(patientId, slotTime, -1)) { return null; }
        String sql = "INSERT INTO appointments (patient_id, service_id, slot_time, status, created_at, updated_at) VALUES (?, ?, ?, 'BOOKED', NOW(), NOW())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, patientId);
            ps.setInt(2, serviceId);
            ps.setTimestamp(3, Timestamp.valueOf(slotTime));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) { return findById(rs.getInt(1)); }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
        return null;
    }

    public synchronized boolean rescheduleAppointment(int appointmentId, int patientId, LocalDateTime newSlot) {
        if (hasDoubleBooking(patientId, newSlot, appointmentId)) { return false; }
        String sql = "UPDATE appointments SET slot_time = ?, status = 'RESCHEDULED', updated_at = NOW() WHERE id = ? AND patient_id = ? AND status <> 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(newSlot));
            ps.setInt(2, appointmentId);
            ps.setInt(3, patientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public synchronized boolean cancelAppointment(int appointmentId, int patientId) {
        String sql = "UPDATE appointments SET status = 'CANCELLED', updated_at = NOW() WHERE id = ? AND patient_id = ? AND status <> 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.setInt(2, patientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public synchronized boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        String sql = "UPDATE appointments SET status = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public synchronized boolean cancelByClinic(int appointmentId, String reason) {
        String sql = "UPDATE appointments SET status = 'CANCELLED_BY_CLINIC', cancellation_reason = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public List<Appointment> findTodayAppointments() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE slot_time BETWEEN ? AND ? ORDER BY slot_time ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(start));
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(map(rs)); }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
        return list;
    }

    public Appointment findById(int appointmentId) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) { return map(rs); }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
        return null;
    }

    public List<Appointment> findByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY slot_time DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(map(rs)); }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
        return list;
    }

    public List<Appointment> findUpcomingByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? AND slot_time > NOW() AND status NOT IN ('CANCELLED', 'CANCELLED_BY_CLINIC') ORDER BY slot_time ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(map(rs)); }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
        return list;
    }

    private boolean hasDoubleBooking(int patientId, LocalDateTime slotTime, int excludeId) {
        String sql = excludeId > 0 ? "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND slot_time = ? AND status NOT IN ('CANCELLED', 'CANCELLED_BY_CLINIC') AND id <> ?" : "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND slot_time = ? AND status NOT IN ('CANCELLED', 'CANCELLED_BY_CLINIC')";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setTimestamp(2, Timestamp.valueOf(slotTime));
            if (excludeId > 0) ps.setInt(3, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    private Appointment map(ResultSet rs) throws SQLException {
        return new Appointment(rs.getInt("id"), rs.getInt("patient_id"), rs.getInt("service_id"), rs.getTimestamp("slot_time").toLocalDateTime(), rs.getString("status"), rs.getTimestamp("created_at").toLocalDateTime(), rs.getTimestamp("updated_at").toLocalDateTime());
    }
}