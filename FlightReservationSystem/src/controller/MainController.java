/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package controller;

import service.*;
import model.*;
import database.DatabaseManager;
import view.MainView;
import view.LoginPanel;
import javafx.stage.Stage;
import java.util.Date;
import java.util.List;

/**
 * Main application controller that coordinates between views, services, and data access layers.
 * Handles user authentication, navigation, and business operations, acting as the central hub
 * for all application logic and data manipulation.
 */
public class MainController {
    private DatabaseManager dbManager;
    private FlightService flightService;
    private ReservationService reservationService;
    private PaymentService paymentService;
    private UserService userService;
    private AircraftService aircraftService;

    private MainView mainView;

    /**
     * Initializes the MainController and sets up all necessary services.
     * This constructor creates instances of the database manager and all service classes.
     */
    public MainController() {
        this.dbManager = DatabaseManager.getInstance();
        this.flightService = new FlightService(dbManager);
        this.reservationService = new ReservationService(dbManager);
        this.paymentService = new PaymentService(dbManager);
        this.userService = new UserService(dbManager);
        this.aircraftService = new AircraftService(dbManager);
    }

    /**
     * Starts the application by displaying the login panel.
     * @param primaryStage The primary stage of the JavaFX application
     */
    public void start(Stage primaryStage) {
        // Show login panel first
        showLogin(primaryStage);
    }

    /**
     * Displays the login form to users for authentication.
     * @param primaryStage The primary stage of the JavaFX application
     */
    public void showLogin(Stage primaryStage) {  // Changed from private to public
        LoginPanel loginPanel = new LoginPanel(this, primaryStage);
        loginPanel.show();
    }

    /**
     * Displays the main application interface after successful authentication and
     * displays role-specific panels based on user permissions.
     * @param user The authenticated User object with role information
     * @param primaryStage The primary stage of the JavaFX application
     */
    public void showMainApplication(User user, Stage primaryStage) {
        mainView = new MainView(this, user, primaryStage);
        mainView.showView();
    }

    // Flight operations

    /**
     * Retrieves all flights from the database so that it can be displayed in flight search
     * and flight management.
     * @return List of all Flight objects
     */
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    /**
     * Searches for flights based on origin, destination, or date.
     * @param origin The departure airport code or city name
     * @param destination The arrival airport code or city name
     * @param date The scheduled departure date
     * @return List of Flight objects matching the search criteria
     */
    public List<Flight> searchFlights(String origin, String destination, Date date) {
        return flightService.searchFlights(origin, destination, date);
    }

    /**
     * Creates a new flight record in the database.
     * Generates auto-incremented flight_id after insertion is successful.
     * @param flight The Flight object containing schedule details
     * @return true if flight creation was successful, false otherwise
     */
    public boolean addFlight(Flight flight) {
        return flightService.addFlight(flight);
    }

    /**
     * Updates an existing flight's information in the database.
     * Uses the flight_id from the provided Flight object to identify the record to update.
     * @param flight The Flight object containing updated details
     * @return true if update was successful, false otherwise
     */
    public boolean updateFlight(Flight flight) {
        return flightService.updateFlight(flight);
    }

    /**
     * Deletes a flight from the database.
     * This operation removes the flight schedule but does not change existing reservations.
     * @param id The unique identifier of the flight to delete
     * @return true if delete was successful, false otherwise
     */
    public boolean deleteFlight(int id) {
        return flightService.deleteFlight(id);
    }

    /**
     * Retrieves a specific flight by its flightId.
     * Used when viewing or editing a flight's details.
     * @param flightId The unique identifier of the flight
     * @return Flight object if found, null otherwise
     */
    public Flight getFlightById(int flightId) {
        return flightService.getFlightById(flightId);
    }

    /**
     * Retrieves all reservations from the database.
     * Used in management panels to display the history of reservations.
     * @return List of all Reservation objects
     */
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    /**
     * Retrieves all reservations for a specific customer.
     * Used to display the booking history for a customer.
     * @param customerId The unique identifier of the customer
     * @return List of Reservation objects for a specific customer
     */
    public List<Reservation> getReservationsByCustomerId(int customerId) {
        return reservationService.getReservationsByCustomerId(customerId);
    }

    /**
     * Creates a new reservation in the database.
     * Generates auto-incremented reservation_id after the insertion is successful.
     * @param reservation The Reservation object containing booking details
     * @return true if reservation was added successfully, false otherwise
     */
    public boolean addReservation(Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

    /**
     * Updates the information for an existing reservation in the database.
     * @param reservation The Reservation object containing updated details
     * @return true if the reservation update was successful, false otherwise
     */
    public boolean updateReservation(Reservation reservation) {
        return reservationService.updateReservation(reservation);
    }

    /**
     * Deletes a reservation from the database.
     * This operation removes the reservation record but does not affect seat availability.
     * @param id The unique identifier of the reservation to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReservation(int id) {
        return reservationService.deleteReservation(id);
    }

    // Payment operations

    /**
     * Retrieves all payment information from the database.
     * Used in payment management panels to display complete transaction history.
     * @return List of all Payment objects
     */
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    /**
     * Creates a new payment record in the database.
     * This method links payments to reservations and updates transaction details.
     * @param payment The Payment object containing transaction information
     * @return true if payment was successful, false otherwise
     */
    public boolean addPayment(Payment payment) {
        return paymentService.addPayment(payment);
    }

    /**
     * Retrieves all users from the database.
     * Used in user management panels to display user listings.
     * @return List of all User objects
     */
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Creates a new user account in the database.
     * Generates auto-incremented user_id after successfully inserting.
     * @param user The User object containing registration details
     * @return true if user creation was successful, false otherwise
     */
    public boolean addUser(User user) {
        return userService.addUser(user);
    }

    /**
     * Updates the information for an existing user in the database.
     * @param user The User object containing updated details
     * @return true if the user update was successful, false otherwise
     */
    public boolean updateUser(User user) {
        return userService.updateUser(user);
    }

    /**
     * Deletes a user account from the database.
     * This operation removes all associated user data and can not be undone.
     * @param id The unique identifier of the user to delete
     * @return true if user deletion was successful, false otherwise
     */
    public boolean deleteUser(int id) {
        return userService.deleteUser(id);
    }

    /**
     * Retrieves all aircraft from the database.
     * Used in aircraft management panels to display a listing of the aircraft.
     * @return List of all Aircraft objects
     */
    public List<Aircraft> getAllAircraft() {
        return aircraftService.getAllAircraft();
    }

    /**
     * Retrieves a specific aircraft by its unique identifier.
     * Used when modifying or viewing aircraft fleet details.
     * @param id The unique identifier of the aircraft
     * @return Aircraft object if found, null otherwise
     */
    public Aircraft getAircraftById(int id) {
        return aircraftService.getAircraftById(id);
    }

    /**
     * Creates a new aircraft record in the database.
     * Generates auto-incremented aircraft_id after successfully inserting.
     * @param aircraft The Aircraft object containing fleet details
     * @return true if aircraft creation was successful, false otherwise
     */
    public boolean addAircraft(Aircraft aircraft) {
        return aircraftService.addAircraft(aircraft);
    }

    /**
     * Updates the information for an existing aircraft.
     * Uses the aircraft_id to identify which record to update.
     * @param aircraft The Aircraft object containing updated details
     * @return true if aircraft update was successful, false otherwise
     */
    public boolean updateAircraft(Aircraft aircraft) {
        return aircraftService.updateAircraft(aircraft);
    }

    /**
     * Deletes an aircraft from the database.
     * This operation removes the aircraft from the fleet.
     * @param id The unique identifier of the aircraft to delete
     * @return true if aircraft deletion was successful, false otherwise
     */
    public boolean deleteAircraft(int id) {
        return aircraftService.deleteAircraft(id);
    }

    /**
     * Retrieves user information by their username for authentication purposes.
     * This method is used during login to verify credentials against the database.
     * @param username The unique username to search for
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }
}
