package ict.db;

import ict.bean.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDB {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public NotificationDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) {}
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public boolean create(int userId, String message, String type) {
        String sql = "INSERT INTO notifications (user_id, message, type, is_read, created_at) VALUES (?, ?, ?, FALSE, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.setString(3, type);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { return false; }
    }

    public List<Notification> findByUser(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Notification(rs.getInt("id"), rs.getInt("user_id"), rs.getString("message"), rs.getString("type"), rs.getTimestamp("created_at").toLocalDateTime(), rs.getBoolean("is_read")));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public List<Notification> findUnreadByUser(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Notification(rs.getInt("id"), rs.getInt("user_id"), rs.getString("message"), rs.getString("type"), rs.getTimestamp("created_at").toLocalDateTime(), rs.getBoolean("is_read")));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public boolean markAllRead(int userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { return false; }
    }
}