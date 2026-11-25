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

import model.Flight;
import dao.FlightDAO;
import database.DatabaseManager;
import java.util.List;

/**
 * Service layer for flight management.
 * Handles flight scheduling and availability tracking.
 */
public class FlightService {
    private FlightDAO flightDAO;

    /**
     * Initializes FlightService with a database manager.
     * @param dbManager The database manager instance
     */
    public FlightService(DatabaseManager dbManager) {
        this.flightDAO = new FlightDAO(dbManager);
    }

    /**
     * Retrieves all flights from the database for to be displayed in search and management views.
     * Used in flight management panels to show the flight schedule.
     * @return List of all Flight objects
     */
    public List<Flight> getAllFlights() {
        return flightDAO.getAllFlights();
    }

    /**
     * Searches for flights based on a specified criteria such as origin, destination, date and airline.
     * @param origin The departure airport code or the city name
     * @param destination The arrival airport code or the city name
     * @param date The departure date
     * @param airline The airline to filter by
     * @return List of Flight objects that match the search criteria
     */
    public List<Flight> searchFlights(String origin, String destination, java.util.Date date, String airline) {
        return flightDAO.searchFlights(origin, destination, date, airline);
    }

    /**
     * Creates a new flight.
     * Generates auto-incremented flight_id after successfully inserting.
     * @param flight The Flight object containing schedule information
     * @return true if flight creation was successful, false otherwise
     */
    public boolean addFlight(Flight flight) {
        return flightDAO.addFlight(flight);
    }

    /**
     * Updates the information of an existing flight in the database.
     * Uses the flight_id from the provided Flight object to identify which record is to be updated.
     * @param flight The Flight object containing updated information
     * @return true if flight update was successful, false otherwise
     */
    public boolean updateFlight(Flight flight) {
        return flightDAO.updateFlight(flight);
    }

    /**
     * Deletes a flight.
     * This operation deletes the flight schedule but does not modify existing reservations.
     * @param id The unique identifier for the flight to delete
     * @return true if flight deletion was successful, false otherwise
     */
    public boolean deleteFlight(int id) {
        return flightDAO.deleteFlight(id);
    }

    /**
     * Retrieves a specific flight using its unique identifier.
     * Used when viewing or editing individual flight information.
     * @param flightId The unique identifier for the flight
     * @return Flight object if found, null otherwise
     */
    public Flight getFlightById(int flightId) {
        return flightDAO.getFlightById(flightId);
    }

    /**
     * Updates the number of available seats for a specific flight.
     * Used to adjust seat availability after bookings or after cancellations.
     * @param flightId The unique identifier for the flight
     * @param availableSeats The new number of available seats
     * @return true if the seat number update was successful, false otherwise
     */
    public boolean updateAvailableSeats(int flightId, int availableSeats) {
        return flightDAO.updateAvailableSeats(flightId, availableSeats);
    }
}
