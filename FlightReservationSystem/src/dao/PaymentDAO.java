/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package dao;

import model.Payment;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Payment entities.
 * Manages reservation payment records.
 */
public class PaymentDAO {
    private DatabaseManager dbManager;

    /**
     * Initializes the PaymentDAO with a database connection manager.
     * @param dbManager The database manager instance
     */
    public PaymentDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Retrieves all payments from the database.
     * Used in payment management panels to display transaction history.
     * @return List of all Payment objects
     */
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setReservationId(rs.getInt("reservation_id"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setTransactionId(rs.getString("transaction_id"));
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    /**
     * Creates a payment record in the database.
     * This method links payments to reservations and saves transaction details.
     * @param payment The Payment object containing the transaction information
     * @return true if payment creation was successful, false otherwise
     */
    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments (reservation_id, payment_date, amount, payment_method, transaction_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getReservationId());
            stmt.setTimestamp(2, new Timestamp(payment.getPaymentDate().getTime()));
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentMethod());
            stmt.setString(5, payment.getTransactionId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the information for an existing payment in the database.
     * Uses the payment_id from the provided Payment object to identify which record is to be updated.
     * @param payment The Payment object containing updated information
     * @return true if payment update was successful, false otherwise
     */
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payments SET reservation_id = ?, payment_date = ?, amount = ?, payment_method = ?, transaction_id = ? WHERE payment_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getReservationId());
            stmt.setTimestamp(2, new Timestamp(payment.getPaymentDate().getTime()));
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentMethod());
            stmt.setString(5, payment.getTransactionId());
            stmt.setInt(6, payment.getPaymentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a payment record from the database.
     * This operation deletes the transaction record but does not modify the reservation.
     * @param id The unique identifier of the payment to delete
     * @return true if payment deletion was successful, false otherwise
     */
    public boolean deletePayment(int id) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";

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
