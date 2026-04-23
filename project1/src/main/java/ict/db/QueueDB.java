package ict.db;

import ict.bean.QueueEntry;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QueueDB {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public QueueDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) {}
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public List<QueueEntry> findWaitingQueue() {
        List<QueueEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM queue_entries WHERE status IN ('WAITING', 'CALLING') ORDER BY queue_number ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapQueue(rs));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public synchronized boolean updateQueueStatus(int id, String status) {
        String sql = "UPDATE queue_entries SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { return false; }
    }

    public List<QueueEntry> joinTodayQueue(int patientId, int serviceId) {
        int nextNumber = 1000 + (int)(Math.random() * 900); 
        String sql = "INSERT INTO queue_entries (patient_id, clinic_id, service_id, queue_date, queue_number, status, joined_at) VALUES (?, 1, ?, CURDATE(), ?, 'WAITING', NOW())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, serviceId);
            ps.setInt(3, nextNumber);
            ps.executeUpdate();
            return findTodayByPatient(patientId);
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public List<QueueEntry> findTodayByPatient(int patientId) {
        List<QueueEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM queue_entries WHERE patient_id = ? AND queue_date = CURDATE() AND status IN ('WAITING', 'CALLING')";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapQueue(rs));
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public int estimateWaitMinutes(int clinicId, int queueNumber) {
        return 15;
    }

    private QueueEntry mapQueue(ResultSet rs) throws SQLException {
        return new QueueEntry(
            rs.getInt("id"),
            rs.getInt("patient_id"),
            rs.getInt("clinic_id"),
            rs.getInt("service_id"),
            rs.getDate("queue_date").toLocalDate(),
            rs.getInt("queue_number"),
            rs.getString("status"),
            rs.getTimestamp("joined_at").toLocalDateTime()
        );
    }
}