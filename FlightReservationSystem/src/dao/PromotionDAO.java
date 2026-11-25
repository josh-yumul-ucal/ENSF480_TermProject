/**
 * ENSF480 Group 1
 * Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package dao;

import model.Promotion;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Promotion entities.
 * Manages promotion news records including creation, removal, and modification.
 */
public class PromotionDAO {
    private DatabaseManager dbManager;

    /**
     * Initializes the PromotionDAO with a database connection manager.
     * @param dbManager The database manager instance
     */
    public PromotionDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Retrieves promotions from the database.
     * @return List of the Promotion objects
     */
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM promotions";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = new Promotion();
                promotion.setPromotionId(rs.getInt("promotion_id"));
                promotion.setPromotionDate(rs.getTimestamp("promotion_date"));
                promotion.setPromotionText(rs.getString("promotion_text"));
                promotions.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    /**
     * Creates a promotion record in the database.
     * @param promotion The Promotion object containing news information
     * @return true If the promotion creation was successful, false otherwise
     */
    public boolean addPromotion(Promotion promotion) {
        String sql = "INSERT INTO promotions (promotion_date, promotion_text) VALUES (?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, new Timestamp(promotion.getPromotionDate().getTime()));
            stmt.setString(2, promotion.getPromotionText());

            boolean result = stmt.executeUpdate() > 0;

            // If needed, get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    promotion.setPromotionId(generatedKeys.getInt(1));
                }
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the promotion in the database.
     * @param promotion The Promotion object containing the updated information
     * @return true If the promotion update was successful, false otherwise
     */
    public boolean updatePromotion(Promotion promotion) {
        String sql = "UPDATE promotions SET promotion_date = ?, promotion_text = ? WHERE promotion_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(promotion.getPromotionDate().getTime()));
            stmt.setString(2, promotion.getPromotionText());
            stmt.setInt(3, promotion.getPromotionId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a promotion from the database.
     * @param id The promotion ID to delete
     * @return true If the promotion deletion was successful, false otherwise
     */
    public boolean deletePromotion(int id) {
        String sql = "DELETE FROM promotions WHERE promotion_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

