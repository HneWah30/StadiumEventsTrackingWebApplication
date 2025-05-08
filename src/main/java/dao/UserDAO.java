package dao;

import model.user.User;
import model.user.Role;
import java.sql.*;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    // Fetch a user by their email
    public static User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleStr = rs.getString("role");
                Role role;

                try {
                    role = Role.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid role in DB: " + roleStr + ". Defaulting to USER.");
                    role = Role.USER;
                }

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        role
                );

                logger.info("User found in database: " + user.getEmail());
                return user;
            } else {
                logger.info("No user found with email: " + email);
            }

        } catch (SQLException e) {
            logger.severe("SQL Error in getUserByEmail: " + e.getMessage());
        }
        return null;
    }

    // Fetch a user by their ID
    public static User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleStr = rs.getString("role");
                Role role;

                try {
                    role = Role.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid role in DB: " + roleStr + ". Defaulting to USER.");
                    role = Role.USER;
                }

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        role
                );

                logger.info("User found in database: " + user.getEmail());
                return user;
            } else {
                logger.info("No user found with ID: " + userId);
            }

        } catch (SQLException e) {
            logger.severe("SQL Error in getUserById: " + e.getMessage());
        }
        return null;
    }

    // Register a new user
    public static boolean registerUser(User user) {
        String query = "INSERT INTO users (username, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail().toLowerCase());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, user.getRole().name());

            int rowsInserted = stmt.executeUpdate();
            logger.info("User registered: " + user.getEmail());
            return rowsInserted > 0;

        } catch (SQLException e) {
            logger.severe("SQL Error in registerUser: " + e.getMessage());
        }
        return false;
    }
}
