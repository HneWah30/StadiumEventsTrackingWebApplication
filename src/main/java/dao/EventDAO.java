package dao;

import model.Event;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class EventDAO {
    private static final Logger logger = Logger.getLogger(EventDAO.class.getName());

    public static List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events ORDER BY date_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching all events: " + e.getMessage());
        }
        return events;
    }

    public static boolean addEvent(Event event) {
        String query = "INSERT INTO events (name, date_time, type, description, tickets_available) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, event.getName());
            stmt.setTimestamp(2, event.getDateTime());
            stmt.setString(3, event.getType());
            stmt.setString(4, event.getDescription());
            stmt.setInt(5, event.getTicketsAvailable());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Event> getRecommendedEvents(int userId) {
        List<Event> recommendations = new ArrayList<>();
        String query = "SELECT * FROM events WHERE type IN (" +
                "SELECT DISTINCT e.type FROM events e " +
                "JOIN user_favorites uf ON e.id = uf.event_id " +
                "WHERE uf.user_id = ?) ORDER BY date_time LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recommendations.add(mapResultSetToEvent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendations;
    }

    public static boolean reduceTickets(int eventId, int quantity) {
        String sql = "UPDATE events SET tickets_available = tickets_available - ? WHERE id = ? AND tickets_available >= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, eventId);
            stmt.setInt(3, quantity);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ NEW: Get available tickets for an event
    public static int getTicketsAvailable(int eventId) {
        String query = "SELECT tickets_available FROM events WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tickets_available");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error fetching ticket count: " + e.getMessage());
        }
        return 0;
    }

    public static List<Event> getFilteredEvents(String eventType, Timestamp startDate, Timestamp endDate) {
        List<Event> events = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM events WHERE 1=1");

        if (eventType != null && !eventType.isEmpty()) query.append(" AND type = ?");
        if (startDate != null) query.append(" AND date_time >= ?");
        if (endDate != null) query.append(" AND date_time <= ?");
        query.append(" ORDER BY date_time");

        logger.info("Final Query: " + query.toString());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int index = 1;
            if (eventType != null && !eventType.isEmpty()) stmt.setString(index++, eventType);
            if (startDate != null) stmt.setTimestamp(index++, startDate);
            if (endDate != null) stmt.setTimestamp(index++, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapResultSetToEvent(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error fetching filtered events: " + e.getMessage());
        }

        return events;
    }

    public static Event getEventById(int eventId) {
        String query = "SELECT * FROM events WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEvent(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error fetching event by ID: " + e.getMessage());
        }
        return null;
    }

    public static boolean deleteEvent(int eventId) {
        String query = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error deleting event: " + e.getMessage());
        }
        return false;
    }

    private static Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getTimestamp("date_time"),
                rs.getString("type"),
                rs.getString("description"),
                rs.getInt("tickets_available")
        );
    }
}
