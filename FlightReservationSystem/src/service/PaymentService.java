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

import model.Payment;
import dao.PaymentDAO;
import database.DatabaseManager;
import java.util.List;

/**
 * Service layer for payment management.
 * Handles payment processing and transaction tracking.
 */
public class PaymentService {
    private PaymentDAO paymentDAO;

    /**
     * Initializes the PaymentService using a database manager.
     * @param dbManager The database manager instance
     */
    public PaymentService(DatabaseManager dbManager) {
        this.paymentDAO = new PaymentDAO(dbManager);
    }

    /**
     * Retrieves all payments from the database.
     * Used in payment management panels to display transaction history.
     * @return List of all Payment objects
     */
    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    /**
     * Creates a new payment record.
     * This method links the payments with reservations and records transaction information.
     * @param payment The Payment object containing the transaction information
     * @return true if payment creation was successful, false otherwise
     */
    public boolean addPayment(Payment payment) {
        return paymentDAO.addPayment(payment);
    }

    /**
     * Updates the information for an existing payment in the database.
     * Uses the payment_id from the provided Payment object to identify which record is to be updated.
     * @param payment The Payment object containing updated details
     * @return true if payment update was successful, false otherwise
     */
    public boolean updatePayment(Payment payment) {
        return paymentDAO.updatePayment(payment);
    }

    /**
     * Deletes a payment record.
     * This operation deletes the transaction record but does not modify the reservation.
     * @param id The unique identifier for the payment that is to be deleted
     * @return true if payment deletion was successful, false otherwise
     */
    public boolean deletePayment(int id) {
        return paymentDAO.deletePayment(id);
    }
}
