package dao;

import model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private final Connection conn;

    public ReservationDAO(Connection conn) {
        this.conn = conn;
    }

    // Fetch all reservations for a user, including event name
    public List<Reservation> getUserReservations(int userId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT r.id, r.user_id, r.event_id, r.quantity, r.status, r.reservation_time, " +
                     "e.name AS event_name " +
                     "FROM reservations r " +
                     "JOIN events e ON r.event_id = e.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.reservation_time DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setUserId(rs.getInt("user_id"));
                res.setEventId(rs.getInt("event_id"));
                res.setQuantity(rs.getInt("quantity"));
                res.setStatus(rs.getString("status"));
                res.setReservationTime(rs.getTimestamp("reservation_time"));
                res.setEventName(rs.getString("event_name"));
                reservations.add(res);
            }
        }

        return reservations;
    }

    // Reserve tickets for a user
    public boolean reserveTickets(int userId, int eventId, int quantity) throws SQLException {
        String updateEventSQL = "UPDATE events SET tickets_available = tickets_available - ? WHERE id = ? AND tickets_available >= ?";
        String insertReservationSQL = "INSERT INTO reservations (user_id, event_id, quantity, status, reservation_time) VALUES (?, ?, ?, 'PENDING', NOW())";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement updateEventStmt = conn.prepareStatement(updateEventSQL)) {
                updateEventStmt.setInt(1, quantity);
                updateEventStmt.setInt(2, eventId);
                updateEventStmt.setInt(3, quantity);
                int rowsAffected = updateEventStmt.executeUpdate();

                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // Not enough tickets available
                }
            }

            try (PreparedStatement insertReservationStmt = conn.prepareStatement(insertReservationSQL)) {
                insertReservationStmt.setInt(1, userId);
                insertReservationStmt.setInt(2, eventId);
                insertReservationStmt.setInt(3, quantity);
                insertReservationStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Get reservation details by ID
    public Reservation getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT r.id, r.user_id, r.event_id, r.quantity, r.status, r.reservation_time, " +
                     "e.name AS event_name " +
                     "FROM reservations r " +
                     "JOIN events e ON r.event_id = e.id " +
                     "WHERE r.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setUserId(rs.getInt("user_id"));
                res.setEventId(rs.getInt("event_id"));
                res.setQuantity(rs.getInt("quantity"));
                res.setStatus(rs.getString("status"));
                res.setReservationTime(rs.getTimestamp("reservation_time"));
                res.setEventName(rs.getString("event_name"));
                return res;
            } else {
                return null;
            }
        }
    }

    // Update the reservation status (e.g., Confirm, Cancel)
    public boolean updateReservationStatus(int reservationId, String status) throws SQLException {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, reservationId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    // Static version for servlet use without DAO instantiation
    public static boolean confirmReservation(int reservationId) {
        String sql = "UPDATE reservations SET status = 'CONFIRMED' WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Link reservation to a purchase (used after successful payment)
    public boolean linkReservationToPurchase(int userId, int eventId, int quantity, int purchaseId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'CONFIRMED', purchase_id = ? " +
                     "WHERE user_id = ? AND event_id = ? AND quantity = ? AND status = 'PENDING' " +
                     "ORDER BY reservation_time ASC LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, purchaseId);
            stmt.setInt(2, userId);
            stmt.setInt(3, eventId);
            stmt.setInt(4, quantity);

            int updated = stmt.executeUpdate();
            return updated > 0;
        }
    }
}
