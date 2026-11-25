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

package service;

import model.Promotion;
import dao.PromotionDAO;
import database.DatabaseManager;
import java.util.List;

/**
 * Service layer for promotion management.
 * Handles the business logic related to promotional announcements.
 */
public class PromotionService {
    private PromotionDAO promotionDAO;

    /**
     * Initializes the PromotionService using a database manager.
     * @param dbManager The database manager instance
     */
    public PromotionService(DatabaseManager dbManager) {
        this.promotionDAO = new PromotionDAO(dbManager);
    }

    /**
     * Retrieves promotions from the database.
     * @return List of all Promotion objects
     */
    public List<Promotion> getAllPromotions() {
        return promotionDAO.getAllPromotions();
    }

    /**
     * Creates a promotion record in the database.
     * @param promotion The Promotion object containing the news information
     * @return true If the promotion creation was successful, false otherwise
     */
    public boolean addPromotion(Promotion promotion) {
        return promotionDAO.addPromotion(promotion);
    }

    /**
     * Updates the information for a promotion in the database.
     * @param promotion The Promotion object containing updated information
     * @return true If the promotion update was successful, false otherwise
     */
    public boolean updatePromotion(Promotion promotion) {
        return promotionDAO.updatePromotion(promotion);
    }

    /**
     * Deletes a promotion from the database.
     * @param id The ID of the promotion to delete
     * @return true If the promotion deletion was successful, false otherwise
     */
    public boolean deletePromotion(int id) {
        return promotionDAO.deletePromotion(id);
    }
}

