package dao;

import java.sql.*;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/stadium_events_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Myjavaclass45@2025";  // Consider storing this in a config file or environment variable

    // Establish and return database connection
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("✅ Database connected successfully!");
        } catch (ClassNotFoundException e) {
            logger.severe("❌ JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            logger.severe("❌ Database connection failed: " + e.getMessage());
        }
        return conn;
    }
}
