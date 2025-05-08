package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Event;

public class FavoriteDAO {

    // Add a favorite event for the user
    public static boolean addFavoriteEvent(int userId, int eventId) {
        String query = "INSERT INTO user_favorites (user_id, event_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead of printStackTrace()
            return false;
        }
    }

    // Remove a favorite event for the user
    public static boolean removeFavoriteEvent(int userId, int eventId) {
        String query = "DELETE FROM user_favorites WHERE user_id = ? AND event_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead of printStackTrace()
            return false;
        }
    }

    // Retrieve the IDs of all favorite events for a given user
    public static List<Integer> getFavoriteEventIds(int userId) {
        List<Integer> favoriteIds = new ArrayList<>();
        String query = "SELECT event_id FROM user_favorites WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    favoriteIds.add(rs.getInt("event_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead of printStackTrace()
        }
        return favoriteIds;
    }

    // Retrieve the full Event objects for all the favorite events of a user
    public static List<Event> getFavoritesByUserId(int userId) {
        List<Event> favoriteEvents = new ArrayList<>();
        String query = "SELECT e.* FROM events e JOIN user_favorites uf ON e.id = uf.event_id WHERE uf.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("dateTime"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("ticketsAvailable")
                    );
                    favoriteEvents.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead of printStackTrace()
        }
        return favoriteEvents;
    }
}
