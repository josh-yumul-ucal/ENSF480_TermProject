/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package service;

import model.Reservation;
import dao.ReservationDAO;
import database.DatabaseManager;
import java.util.List;

/**
 * Service layer for management of reservations.
 * Handles reservation management including retrieval and booking management.
 */
public class ReservationService {
    private ReservationDAO reservationDAO;

    /**
     * Initializes the ReservationService using a database manager.
     * @param dbManager The database manager instance
     */
    public ReservationService(DatabaseManager dbManager) {
        this.reservationDAO = new ReservationDAO(dbManager);
    }

    /**
     * Retrieves all reservations from the database.
     * Used in management panels to display reservation history.
     * @return List of all Reservation objects
     */
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    /**
     * Retrieves all reservations that are associated with a specific customer.
     * Used to display the booking history for a customer.
     * @param customerId The unique identifier for the customer
     * @return List of Reservation objects for the specified customer
     */
    public List<Reservation> getReservationsByCustomerId(int customerId) {
        return reservationDAO.getReservationsByCustomerId(customerId);
    }

    /**
     * Creates a new reservation.
     * Generates auto-incremented reservation_id after successfully inserting.
     * @param reservation The Reservation object containing booking information
     * @return true if reservation creation was successful, false otherwise
     */
    public boolean addReservation(Reservation reservation) {
        return reservationDAO.addReservation(reservation);
    }

    /**
     * Updates the information for an existing reservation in the database.
     * Uses the reservation_id from the provided Reservation object to identify which record is to be updated.
     * @param reservation The Reservation object containing updated information
     * @return true if reservation update was successful, false otherwise
     */
    public boolean updateReservation(Reservation reservation) {
        return reservationDAO.updateReservation(reservation);
    }

    /**
     * Deletes a reservation.
     * This operation deletes the booking record but does not modify seat availability.
     * @param id The unique identifier for the reservation that is to be deleted
     * @return true if reservation deletion was successful, false otherwise
     */
    public boolean deleteReservation(int id) {
        return reservationDAO.deleteReservation(id);
    }

    /**
     * Calculates the number of reserved seats for a specific flight.
     * Used to prevent overbooking by checking the seat availability before new reservations.
     * @param flightId The unique identifier for the flight
     * @return The number of reserved seats for the flight
     */
    public int getReservedSeatsForFlight(int flightId) {
        return reservationDAO.getReservedSeatsForFlight(flightId);
    }
}
