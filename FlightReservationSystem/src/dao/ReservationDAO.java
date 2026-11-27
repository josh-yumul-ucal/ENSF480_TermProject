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

import model.Reservation;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Reservation entities.
 * Manages reservation creation, retrieval, and modification including
 * booking information and seat availability tracking.
 */
public class ReservationDAO {
    private DatabaseManager dbManager;

    /**
     * Initializes the ReservationDAO with a database connection manager.
     * @param dbManager The database manager instance
     */
    public ReservationDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Retrieves all reservations from the database.
     * Used in management panels to display reservation history.
     * @return List of all Reservation objects
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setCustomerId(rs.getInt("customer_id"));
                reservation.setFlightId(rs.getInt("flight_id"));
                reservation.setBookingDate(rs.getTimestamp("booking_date"));
                reservation.setNumberOfSeats(rs.getInt("number_of_seats"));
                reservation.setTotalAmount(rs.getDouble("total_amount"));
                reservation.setPaymentMethod(rs.getString("payment_method"));
                reservation.setTransactionId(rs.getString("transaction_id"));
                reservation.setCardholderName(rs.getString("cardholder_name"));
                reservation.setCreditCardNumber(rs.getString("credit_card_number"));
                reservation.setExpiryDate(rs.getString("expiry_date"));
                reservation.setCvv(rs.getString("cvv"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Retrieves all reservations for a specific customer.
     * @param customerId The unique identifier of the customer
     * @return List of Reservation objects for the specified customer
     */
    public List<Reservation> getReservationsByCustomerId(int customerId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE customer_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setCustomerId(rs.getInt("customer_id"));
                    reservation.setFlightId(rs.getInt("flight_id"));
                    reservation.setBookingDate(rs.getTimestamp("booking_date"));
                    reservation.setNumberOfSeats(rs.getInt("number_of_seats"));
                    reservation.setTotalAmount(rs.getDouble("total_amount"));
                    reservation.setPaymentMethod(rs.getString("payment_method"));
                    reservation.setTransactionId(rs.getString("transaction_id"));
                    reservation.setCardholderName(rs.getString("cardholder_name"));
                    reservation.setCreditCardNumber(rs.getString("credit_card_number"));
                    reservation.setExpiryDate(rs.getString("expiry_date"));
                    reservation.setCvv(rs.getString("cvv"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Creates a new reservation record in the database.
     * Generates auto-incremented reservation_id after successfully inserting.
     * @param reservation The Reservation object containing booking information
     * @return true if reservation creation was successful, false otherwise
     */
    public boolean addReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (customer_id, flight_id, booking_date, number_of_seats, total_amount, payment_method, transaction_id, cardholder_name, credit_card_number, expiry_date, cvv) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reservation.getCustomerId());
            stmt.setInt(2, reservation.getFlightId());
            stmt.setTimestamp(3, new Timestamp(reservation.getBookingDate().getTime()));
            stmt.setInt(4, reservation.getNumberOfSeats());
            stmt.setDouble(5, reservation.getTotalAmount());
            stmt.setString(6, reservation.getPaymentMethod());
            stmt.setString(7, reservation.getTransactionId());
            stmt.setString(8, reservation.getCardholderName());
            stmt.setString(9, reservation.getCreditCardNumber());
            stmt.setString(10, reservation.getExpiryDate());
            stmt.setString(11, reservation.getCvv());

            boolean result = stmt.executeUpdate() > 0;

            // If needed, get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setReservationId(generatedKeys.getInt(1));
                }
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the information for an existing reservation in the database.
     * Uses the reservation_id from the provided Reservation object to identify which record is to be updated.
     * @param reservation The Reservation object containing updated details
     * @return true if the reservation update was successful, false otherwise
     */
    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE reservations SET customer_id = ?, flight_id = ?, booking_date = ?, number_of_seats = ?, total_amount = ?, payment_method = ?, transaction_id = ?, cardholder_name = ?, credit_card_number = ?, expiry_date = ?, cvv = ? WHERE reservation_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getCustomerId());
            stmt.setInt(2, reservation.getFlightId());
            stmt.setTimestamp(3, new Timestamp(reservation.getBookingDate().getTime()));
            stmt.setInt(4, reservation.getNumberOfSeats());
            stmt.setDouble(5, reservation.getTotalAmount());
            stmt.setString(6, reservation.getPaymentMethod());
            stmt.setString(7, reservation.getTransactionId());
            stmt.setString(8, reservation.getCardholderName());
            stmt.setString(9, reservation.getCreditCardNumber());
            stmt.setString(10, reservation.getExpiryDate());
            stmt.setString(11, reservation.getCvv());
            stmt.setInt(12, reservation.getReservationId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a reservation record from the database.
     * This operation removes the booking record but does not modify seat availability.
     * @param id The unique identifier of the reservation to delete
     * @return true if reservation deletion was successful, false otherwise
     */
    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calculates the number of reserved seats for a specific flight.
     * Used to prevent overbooking by checking seat availability before new reservations are made.
     * @param flightId The unique identifier of the flight
     * @return Total count of reserved seats for the flight
     */
    public int getReservedSeatsForFlight(int flightId) {
        String sql = "SELECT SUM(number_of_seats) as total_reserved FROM reservations WHERE flight_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_reserved") != 0 ? rs.getInt("total_reserved") : 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
