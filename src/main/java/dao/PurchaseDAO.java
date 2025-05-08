package dao;

import model.Purchase;
import java.sql.*;

public class PurchaseDAO {

    public static int addPurchase(Purchase purchase) {
    String query = "INSERT INTO purchases (user_id, event_id, quantity) VALUES (?, ?, ?)";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        ps.setInt(1, purchase.getUserId());
        ps.setInt(2, purchase.getEventId());
        ps.setInt(3, purchase.getQuantity());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return reservation ID
                }
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}
    
}