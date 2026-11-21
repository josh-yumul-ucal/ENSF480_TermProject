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

import model.Flight;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Flight entities.
 * Manages flight information including schedules, routes, and capacity information.
 * Supports the functionality for search and maintains flight availability.
 */
public class FlightDAO {
    private DatabaseManager dbManager;

    /**
     * Initializes the FlightDAO with a database connection manager.
     * @param dbManager The database manager instance
     */
    public FlightDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Retrieves all flights from the database to display in search and flight management.
     * @return List of all Flight objects
     */
    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setOrigin(rs.getString("origin"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setAircraftId(rs.getInt("aircraft_id"));
                flight.setTotalSeats(rs.getInt("total_seats"));
                flight.setAvailableSeats(rs.getInt("available_seats"));
                flight.setPrice(rs.getDouble("price"));
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    /**
     * Searches for flights based on origin, destination, and date.
     * @param origin The departure airport code or city name
     * @param destination The arrival airport code or city name
     * @param date The scheduled departure date
     * @return List of Flight objects that match the search criteria
     */
    public List<Flight> searchFlights(String origin, String destination, java.util.Date date) {
        // Build dynamic query based on provided parameters
        StringBuilder sql = new StringBuilder("SELECT * FROM flights WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (origin != null && !origin.trim().isEmpty()) {
            sql.append(" AND origin LIKE ?");
            params.add("%" + origin.trim() + "%");
        }

        if (destination != null && !destination.trim().isEmpty()) {
            sql.append(" AND destination LIKE ?");
            params.add("%" + destination.trim() + "%");
        }

        if (date != null) {
            sql.append(" AND DATE(departure_time) = ?");
            params.add(new java.sql.Date(date.getTime()));
        }

        List<Flight> flights = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Flight flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setOrigin(rs.getString("origin"));
                    flight.setDestination(rs.getString("destination"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                    flight.setAircraftId(rs.getInt("aircraft_id"));
                    flight.setTotalSeats(rs.getInt("total_seats"));
                    flight.setAvailableSeats(rs.getInt("available_seats"));
                    flight.setPrice(rs.getDouble("price"));
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    /**
     * Creates a new flight in the database.
     * Generates auto-incremented flight_id after successfully inserting.
     * @param flight The Flight object containing schedule information
     * @return true if flight creation was successful, false otherwise
     */
    public boolean addFlight(Flight flight) {
        String sql = "INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, aircraft_id, total_seats, available_seats, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getOrigin());
            stmt.setString(3, flight.getDestination());
            stmt.setTimestamp(4, new Timestamp(flight.getDepartureTime().getTime()));
            stmt.setTimestamp(5, new Timestamp(flight.getArrivalTime().getTime()));
            stmt.setInt(6, flight.getAircraftId());
            stmt.setInt(7, flight.getTotalSeats());
            stmt.setInt(8, flight.getAvailableSeats());
            stmt.setDouble(9, flight.getPrice());

            boolean result = stmt.executeUpdate() > 0;

            // If needed, get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    flight.setFlightId(generatedKeys.getInt(1));
                }
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the information for an existing flight in the database.
     * Uses the flight_id from the provided Flight object to identify which record to to be updated.
     * @param flight The Flight object containing updated information
     * @return true if flight update was successful, false otherwise
     */
    public boolean updateFlight(Flight flight) {
        String sql = "UPDATE flights SET flight_number = ?, origin = ?, destination = ?, departure_time = ?, arrival_time = ?, aircraft_id = ?, total_seats = ?, available_seats = ?, price = ? WHERE flight_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getOrigin());
            stmt.setString(3, flight.getDestination());
            stmt.setTimestamp(4, new Timestamp(flight.getDepartureTime().getTime()));
            stmt.setTimestamp(5, new Timestamp(flight.getArrivalTime().getTime()));
            stmt.setInt(6, flight.getAircraftId());
            stmt.setInt(7, flight.getTotalSeats());
            stmt.setInt(8, flight.getAvailableSeats());
            stmt.setDouble(9, flight.getPrice());
            stmt.setInt(10, flight.getFlightId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a flight from the database.
     * This operation deletes the flight schedule but does not affect the existing reservations.
     * @param id The unique identifier of the flight to delete
     * @return true if flight deletion was successful, false otherwise
     */
    public boolean deleteFlight(int id) {
        String sql = "DELETE FROM flights WHERE flight_id = ?";

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
     * Retrieves a specific flight using its unique identifier.
     * @param flightId The unique identifier of the flight
     * @return Flight object if found, null otherwise
     */
    public Flight getFlightById(int flightId) {
        String sql = "SELECT * FROM flights WHERE flight_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Flight flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setOrigin(rs.getString("origin"));
                    flight.setDestination(rs.getString("destination"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                    flight.setAircraftId(rs.getInt("aircraft_id"));
                    flight.setTotalSeats(rs.getInt("total_seats"));
                    flight.setAvailableSeats(rs.getInt("available_seats"));
                    flight.setPrice(rs.getDouble("price"));
                    return flight;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the number of available seats for a specified flight.
     * Used to modify seat availability after bookings or cancellations.
     * @param flightId The unique identifier of the flight
     * @param availableSeats The new available number of seats
     * @return true if seat availability update was successful, false otherwise
     */
    public boolean updateAvailableSeats(int flightId, int availableSeats) {
        String sql = "UPDATE flights SET available_seats = ? WHERE flight_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, availableSeats);
            stmt.setInt(2, flightId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
