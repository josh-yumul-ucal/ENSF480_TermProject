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

import model.Aircraft;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Aircraft.
 * Manages fleet information which includes aircraft details and capacity.
 */
public class AircraftDAO {
    private DatabaseManager dbManager;

    /**
     * Initializes the AircraftDAO with a database connection manager.
     * @param dbManager The database manager instance
     */
    public AircraftDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Retrieves all aircraft from the database.
     * Used in aircraft management panels to display all aircraft.
     * @return List of all aircraft objects
     */
    public List<Aircraft> getAllAircraft() {
        List<Aircraft> aircraftList = new ArrayList<>();
        String sql = "SELECT * FROM aircraft";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aircraft aircraft = new Aircraft();
                aircraft.setAircraftId(rs.getInt("aircraft_id"));
                aircraft.setAircraftName(rs.getString("aircraft_name"));
                aircraft.setModel(rs.getString("model"));
                aircraft.setCapacity(rs.getInt("capacity"));
                aircraftList.add(aircraft);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraftList;
    }

    /**
     * Retrieves a specific aircraft using its unique identifier.
     * @param id The unique identifier for the aircraft
     * @return Aircraft object if found, null otherwise
     */
    public Aircraft getAircraftById(int id) {
        String sql = "SELECT * FROM aircraft WHERE aircraft_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Aircraft aircraft = new Aircraft();
                    aircraft.setAircraftId(rs.getInt("aircraft_id"));
                    aircraft.setAircraftName(rs.getString("aircraft_name"));
                    aircraft.setModel(rs.getString("model"));
                    aircraft.setCapacity(rs.getInt("capacity"));
                    return aircraft;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new aircraft in the database.
     * Generates auto-incremented aircraft_id after successfully inserting.
     * @param aircraft The Aircraft object containing fleet information
     * @return true if aircraft creation was successful, false otherwise
     */
    public boolean addAircraft(Aircraft aircraft) {
        String sql = "INSERT INTO aircraft (aircraft_name, model, capacity) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aircraft.getAircraftName());
            stmt.setString(2, aircraft.getModel());
            stmt.setInt(3, aircraft.getCapacity());

            boolean result = stmt.executeUpdate() > 0;

            // If needed, get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    aircraft.setAircraftId(generatedKeys.getInt(1));
                }
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the information for an existing aircraft in the database.
     * Uses the aircraft_id from the provided Aircraft object to identify which record is to updated.
     * @param aircraft The Aircraft object containing updated information
     * @return true if aircraft update was successful, false otherwise
     */
    public boolean updateAircraft(Aircraft aircraft) {
        String sql = "UPDATE aircraft SET aircraft_name = ?, model = ?, capacity = ? WHERE aircraft_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aircraft.getAircraftName());
            stmt.setString(2, aircraft.getModel());
            stmt.setInt(3, aircraft.getCapacity());
            stmt.setInt(4, aircraft.getAircraftId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes an aircraft from the database.
     * This operation removes the aircraft from the fleet.
     * @param id The unique identifier of the aircraft to delete
     * @return true if aircraft deletion was successful, false otherwise
     */
    public boolean deleteAircraft(int id) {
        String sql = "DELETE FROM aircraft WHERE aircraft_id = ?";

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
