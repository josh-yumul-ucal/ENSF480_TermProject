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

import model.Aircraft;
import dao.AircraftDAO;
import database.DatabaseManager;
import java.util.List;

/**
 * Service layer for aircraft management.
 * Handles business logic related to fleet information and aircraft management.
 */
public class AircraftService {
    private AircraftDAO aircraftDAO;

    /**
     * Initializes the AircraftService using a database manager.
     * @param dbManager The database manager instance
     */
    public AircraftService(DatabaseManager dbManager) {
        this.aircraftDAO = new AircraftDAO(dbManager);
    }

    /**
     * Retrieves all aircraft from the database.
     * Used in aircraft management panels to display fleet information.
     * @return List of all Aircraft objects
     */
    public List<Aircraft> getAllAircraft() {
        return aircraftDAO.getAllAircraft();
    }

    /**
     * Retrieves a specific aircraft using its unique identifier.
     * Used when editing or viewing individual aircraft information.
     * @param id The unique identifier of the aircraft
     * @return Aircraft object if found, null otherwise
     */
    public Aircraft getAircraftById(int id) {
        return aircraftDAO.getAircraftById(id);
    }

    /**
     * Creates a new aircraft in the database.
     * Generates auto-incremented aircraft_id after successfully inserting.
     * @param aircraft The Aircraft object containing fleet information
     * @return true if aircraft creation was successful, false otherwise
     */
    public boolean addAircraft(Aircraft aircraft) {
        return aircraftDAO.addAircraft(aircraft);
    }

    /**
     * Updates the information for an existing aircraft in the database.
     * Uses the aircraft_id from the provided Aircraft object to identify which record is to be updated.
     * @param aircraft The Aircraft object containing updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateAircraft(Aircraft aircraft) {
        return aircraftDAO.updateAircraft(aircraft);
    }

    /**
     * Deletes an aircraft.
     * This operation deletes the aircraft from the fleet.
     * @param id The unique identifier of the aircraft that is to be deleted
     * @return true if aircraft deletion was successful, false otherwise
     */
    public boolean deleteAircraft(int id) {
        return aircraftDAO.deleteAircraft(id);
    }
}
