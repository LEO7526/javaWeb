package ict.db;

import ict.bean.EventBean;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EventDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public EventDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("MySQL JDBC driver not found", ex);
        }

        ensureSchema();
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private void ensureSchema() {
        try (Connection conn = getConnection()) {
            createEventTableIfMissing(conn);
            seedSampleData(conn);
        } catch (Exception ex) {
            if (isUnknownDatabase(ex)) {
                createDatabaseThenInitialize();
                return;
            }
            throw new RuntimeException("Failed to initialize EVENTSDB", ex);
        }
    }

    private boolean isUnknownDatabase(Exception ex) {
        Throwable cause = ex;
        while (cause != null) {
            String message = cause.getMessage();
            if (message != null && message.toLowerCase().contains("unknown database")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private void createDatabaseThenInitialize() {
        String dbName = extractDatabaseName(dbUrl);
        String baseUrl = extractBaseUrl(dbUrl);

        if (!dbName.matches("[A-Za-z0-9_]+")) {
            throw new RuntimeException("Unsafe database name: " + dbName);
        }

        String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS `" + dbName + "` "
                + "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        try (Connection baseConn = DriverManager.getConnection(baseUrl, dbUser, dbPassword);
             Statement stmt = baseConn.createStatement()) {
            stmt.executeUpdate(createDatabaseSql);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create database " + dbName, ex);
        }

        try (Connection conn = getConnection()) {
            createEventTableIfMissing(conn);
            seedSampleData(conn);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize tables in database " + dbName, ex);
        }
    }

    private String extractDatabaseName(String jdbcUrl) {
        int protocolEnd = jdbcUrl.indexOf("//");
        int slash = jdbcUrl.indexOf('/', protocolEnd >= 0 ? protocolEnd + 2 : 0);
        if (slash < 0 || slash + 1 >= jdbcUrl.length()) {
            throw new RuntimeException("Invalid JDBC URL, database name missing: " + jdbcUrl);
        }

        String dbPart = jdbcUrl.substring(slash + 1);
        int questionMark = dbPart.indexOf('?');
        if (questionMark >= 0) {
            dbPart = dbPart.substring(0, questionMark);
        }

        return dbPart;
    }

    private String extractBaseUrl(String jdbcUrl) {
        int protocolEnd = jdbcUrl.indexOf("//");
        int slash = jdbcUrl.indexOf('/', protocolEnd >= 0 ? protocolEnd + 2 : 0);
        if (slash < 0) {
            throw new RuntimeException("Invalid JDBC URL: " + jdbcUrl);
        }
        return jdbcUrl.substring(0, slash + 1);
    }

    private void createEventTableIfMissing(Connection conn) throws Exception {
        String createSql = "CREATE TABLE IF NOT EXISTS eventTable ("
                + "eventID INT(10) PRIMARY KEY NOT NULL,"
                + "eventName VARCHAR(100) NOT NULL,"
                + "eventDate DATE NOT NULL,"
                + "venue VARCHAR(150) NOT NULL,"
                + "ticketPrice INT NOT NULL,"
                + "seatsAvailable INT(5) NOT NULL"
                + ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createSql);
        }
    }

    private void seedSampleData(Connection conn) throws Exception {
        seedIfMissing(conn, 1, "Tech Conference 2025", "2025-06-15", "Convention Center", 50, 400);
        seedIfMissing(conn, 2, "Music Fest", "2025-07-20", "Harbour Park", 80, 150);
        seedIfMissing(conn, 3, "Art Exhibition", "2025-08-10", "City Gallery", 30, 50);
        seedIfMissing(conn, 4, "Food Carnival", "2025-09-05", "Harbour Park", 20, 0);
        seedIfMissing(conn, 5, "Startup Summit", "2025-10-01", "Convention Center", 60, 300);
    }

    private void seedIfMissing(Connection conn, int eventID, String eventName, String eventDate,
            String venue, int ticketPrice, int seatsAvailable) throws Exception {
        String existsSql = "SELECT COUNT(*) FROM eventTable WHERE eventID = ?";
        String insertSql = "INSERT INTO eventTable (eventID, eventName, eventDate, venue, ticketPrice, seatsAvailable) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        boolean exists;
        try (PreparedStatement ps = conn.prepareStatement(existsSql)) {
            ps.setInt(1, eventID);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                exists = rs.getInt(1) > 0;
            }
        }

        if (!exists) {
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, eventID);
                ps.setString(2, eventName);
                ps.setString(3, eventDate);
                ps.setString(4, venue);
                ps.setInt(5, ticketPrice);
                ps.setInt(6, seatsAvailable);
                ps.executeUpdate();
            }
        }
    }

    public ArrayList<EventBean> getEvents(String venue) throws Exception {
        ArrayList<EventBean> events = new ArrayList<EventBean>();
        String sql = "SELECT * FROM eventTable WHERE venue = '" + venue + "'";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            EventBean event = new EventBean();
            event.setEventID(rs.getInt("eventID"));
            event.setEventName(rs.getString("eventName"));
            event.setEventDate(rs.getString("eventDate"));
            event.setVenue(rs.getString("venue"));
            event.setTicketPrice(rs.getInt("ticketPrice"));
            event.setSeatsAvailable(rs.getInt("seatsAvailable"));
            events.add(event);
        }

        rs.close();
        stmt.close();
        conn.close();
        return events;
    }
}
