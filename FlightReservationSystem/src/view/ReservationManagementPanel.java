/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package view;

import controller.MainController;
import model.Reservation;
import model.User;
import model.Flight;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * View component for reservation management.
 * Provides a user interface for viewing, adding, updating, and deleting the reservations.
 * Displays the reservation information in a table format with booking information.
 */
public class ReservationManagementPanel extends VBox {
    private MainController controller;
    private User currentUser;

    private ComboBox<User> customerIdComboBox;
    private ComboBox<Flight> flightComboBox;
    private TextField bookingDateField; // Using text field for date input
    private TextField seatsField;
    private TextField amountField;
    private TextField statusField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button refreshButton;
    private TableView<Reservation> reservationTable;
    private ObservableList<Reservation> reservationData;

    /**
     * Initializes the reservation management panel with the controller and UI components.
     * Sets up layout, event handlers, and initial data display.
     * @param controller The main application controller
     */
    public ReservationManagementPanel(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshReservations();
    }

    /**
     * Initializes UI components for the reservation management panel.
     * Sets up the text fields, buttons, and table with column definitions.
     */
    private void initializeComponents() {
        // Create customer dropdown with User objects
        ObservableList<User> customerList = FXCollections.observableArrayList();
        customerIdComboBox = new ComboBox<>(customerList);
        customerIdComboBox.setPromptText("Select Customer");

        // Set converter for customer combo box
        customerIdComboBox.setConverter(new javafx.util.StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user == null) return "";
                return user.getFirstName() + " " + user.getLastName() + " (" + user.getUserId() + ")";
            }

            @Override
            public User fromString(String string) {
                return null; // Not needed for this use case
            }
        });

        // Create flight dropdown
        ObservableList<Flight> flightList = FXCollections.observableArrayList();
        flightComboBox = new ComboBox<>(flightList);
        flightComboBox.setPromptText("Select Flight");

        // Set converter for flight combo box
        flightComboBox.setConverter(new javafx.util.StringConverter<Flight>() {
            @Override
            public String toString(Flight flight) {
                if (flight == null) return "";
                return flight.getFlightNumber() + " (" + flight.getOrigin() + " to " + flight.getDestination() + ")";
            }

            @Override
            public Flight fromString(String string) {
                return null; // Not needed for this use case
            }
        });

        bookingDateField = new TextField();
        bookingDateField.setPromptText("Booking Date (YYYY-MM-DD HH:MM)");

        seatsField = new TextField();
        seatsField.setPromptText("Seats");

        amountField = new TextField();
        amountField.setPromptText("Amount");

        statusField = new TextField();
        statusField.setPromptText("Status");

        addButton = new Button("Add Reservation");
        updateButton = new Button("Update Reservation");
        deleteButton = new Button("Delete Reservation");
        refreshButton = new Button("Refresh");

        reservationData = FXCollections.observableArrayList();
        reservationTable = new TableView<>();

        // Create columns
        TableColumn<Reservation, Integer> customerIdColumn = new TableColumn<>("Customer ID");
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());

        TableColumn<Reservation, Integer> flightIdColumn = new TableColumn<>("Flight ID");
        flightIdColumn.setCellValueFactory(cellData -> cellData.getValue().flightIdProperty().asObject());

        TableColumn<Reservation, Date> bookingDateColumn = new TableColumn<>("Booking Date");
        bookingDateColumn.setCellValueFactory(cellData -> cellData.getValue().bookingDateProperty());

        TableColumn<Reservation, Integer> seatsColumn = new TableColumn<>("Seats");
        seatsColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfSeatsProperty().asObject());

        TableColumn<Reservation, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());

        TableColumn<Reservation, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        reservationTable.getColumns().addAll(customerIdColumn, flightIdColumn, bookingDateColumn,
                seatsColumn, amountColumn, statusColumn);
        reservationTable.setItems(reservationData);
    }

    /**
     * Sets up layout structure for the reservation management panel.
     * Configures the spacing, padding, and arrangement of UI components.
     */
    private void setupLayout() {
        setPadding(new Insets(10));
        setSpacing(10);

        // Input panel
        GridPane inputPanel = new GridPane();
        inputPanel.setPadding(new Insets(10));
        inputPanel.setHgap(10);
        inputPanel.setVgap(10);

        inputPanel.add(new Label("Customer:"), 0, 0);
        inputPanel.add(customerIdComboBox, 1, 0);

        inputPanel.add(new Label("Flight:"), 0, 1);
        inputPanel.add(flightComboBox, 1, 1);

        inputPanel.add(new Label("Booking Date (YYYY-MM-DD HH:MM):"), 0, 2);
        inputPanel.add(bookingDateField, 1, 2);

        inputPanel.add(new Label("Seats:"), 0, 3);
        inputPanel.add(seatsField, 1, 3);

        inputPanel.add(new Label("Amount:"), 0, 4);
        inputPanel.add(amountField, 1, 4);

        inputPanel.add(new Label("Status:"), 0, 5);
        inputPanel.add(statusField, 1, 5);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton);

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(inputPanel, buttonBox);

        // Results panel
        ScrollPane scrollPane = new ScrollPane(reservationTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        VBox resultsBox = new VBox(10);
        resultsBox.getChildren().add(scrollPane);

        getChildren().addAll(inputBox, resultsBox);
    }

    /**
     * Sets up the event handlers for UI interactions.
     * Connects the button clicks and table selection to action methods.
     */
    private void setupEventHandlers() {
        addButton.setOnAction(e -> addReservation());
        updateButton.setOnAction(e -> updateReservation());
        deleteButton.setOnAction(e -> deleteReservation());
        refreshButton.setOnAction(e -> refreshReservations());

        reservationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadReservationData(newSelection);
            }
        });
    }

    /**
     * Sets the current user for role-based filtering
     * @param currentUser The currently logged in user
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        refreshReservations();
    }

    /**
     * Adds a new reservation.
     * Validates the input and refreshes the table display after successful creation.
     */
    private void addReservation() {
        Reservation reservation = createReservationFromInput();
        if (reservation != null) {
            if (controller.addReservation(reservation)) {
                showAlert("Success", "Reservation added successfully");
                refreshReservations();
                clearInputFields();
            } else {
                showAlert("Error", "Failed to add reservation");
            }
        }
    }

    /**
     * Updates an existing reservation.
     * Uses selected item reference to preserve ID during the update operation.
     */
    private void updateReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a reservation to update");
            return;
        }

        Reservation reservation = createReservationFromInput();
        if (reservation != null) {
            // Preserve the ID from the original selection
            reservation.setReservationId(selected.getReservationId());

            if (controller.updateReservation(reservation)) {
                showAlert("Success", "Reservation updated successfully");
                refreshReservations();
            } else {
                showAlert("Error", "Failed to update reservation");
            }
        }
    }

    /**
     * Deletes a reservation.
     * Prompts user confirmation before proceeding with the deletion.
     */
    private void deleteReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a reservation to delete");
            return;
        }

        if (confirmAction("Are you sure you want to delete this reservation?")) {
            if (controller.deleteReservation(selected.getReservationId())) { // Use actual ID
                showAlert("Success", "Reservation deleted successfully");
                refreshReservations();
            } else {
                showAlert("Error", "Failed to delete reservation");
            }
        }
    }

    /**
     * Refreshes the reservation table.
     * This ensures that the auto-generated IDs are properly loaded after record creation.
     */
    private void refreshReservations() {
        List<Reservation> reservations;

        // If current user is a customer, filter reservations by customer ID
        if (currentUser != null && "Customer".equals(currentUser.getRole())) {
            reservations = controller.getReservationsByCustomerId(currentUser.getUserId());
        } else {
            reservations = controller.getAllReservations();
        }

        reservationData.clear();
        reservationData.addAll(reservations);

        // Also populate flight and customer dropdowns
        ObservableList<Flight> flightList = FXCollections.observableArrayList();
        for (Flight flight : controller.getAllFlights()) {
            flightList.add(flight);
        }
        flightComboBox.setItems(flightList);

        // Populate customer dropdown with User objects
        ObservableList<User> userList = FXCollections.observableArrayList();
        List<User> allUsers = controller.getAllUsers();

        // If current user is a customer, only show the current user in the dropdown
        if (currentUser != null && "Customer".equals(currentUser.getRole())) {
            userList.add(currentUser);
        } else {
            userList.addAll(allUsers);
        }
        customerIdComboBox.setItems(userList);
    }

    /**
     * Creates a Reservation object from user input fields.
     * Validates required fields and parses date/time values.
     * @return Reservation object if validation passes, null otherwise
     */
    private Reservation createReservationFromInput() {
        User selectedUser = customerIdComboBox.getValue();
        Flight selectedFlight = flightComboBox.getValue();
        String bookingDateStr = bookingDateField.getText().trim();
        String seatsStr = seatsField.getText().trim();
        String amountStr = amountField.getText().trim();
        String status = statusField.getText().trim();

        if (selectedUser == null || selectedFlight == null || bookingDateStr.isEmpty() ||
                seatsStr.isEmpty() || amountStr.isEmpty() || status.isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return null;
        }

        try {
            int customerId = selectedUser.getUserId();
            int seats = Integer.parseInt(seatsStr);
            double amount = Double.parseDouble(amountStr);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date bookingDate = sdf.parse(bookingDateStr);

            Reservation reservation = new Reservation();
            reservation.setCustomerId(customerId);
            reservation.setFlightId(selectedFlight.getFlightId());
            reservation.setBookingDate(bookingDate);
            reservation.setNumberOfSeats(seats);
            reservation.setTotalAmount(amount);
            reservation.setStatus(status);

            return reservation;
        } catch (Exception e) {
            showAlert("Error", "Please enter valid numbers and a valid date in YYYY-MM-DD HH:MM format");
            return null;
        }
    }

    /**
     * Loads data from Reservation object into the input fields.
     * Used when selecting an item to update its information.
     * @param reservation The Reservation object to load data from
     */
    private void loadReservationData(Reservation reservation) {
        // Set customer in dropdown
        if (reservation.getCustomerId() > 0) {
            for (User user : customerIdComboBox.getItems()) {
                if (user.getUserId() == reservation.getCustomerId()) {
                    customerIdComboBox.setValue(user);
                    break;
                }
            }
        } else {
            customerIdComboBox.setValue(null);
        }

        // Set flight in dropdown
        if (reservation.getFlightId() > 0) {
            for (Flight flight : flightComboBox.getItems()) {
                if (flight.getFlightId() == reservation.getFlightId()) {
                    flightComboBox.setValue(flight);
                    break;
                }
            }
        } else {
            flightComboBox.setValue(null);
        }

        bookingDateField.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(reservation.getBookingDate()));
        seatsField.setText(String.valueOf(reservation.getNumberOfSeats()));
        amountField.setText(String.valueOf(reservation.getTotalAmount()));
        statusField.setText(reservation.getStatus());
    }

    /**
     * Clears all of the input fields in the form.
     * Used after the successful creation or when resetting the form.
     */
    private void clearInputFields() {
        customerIdComboBox.setValue(null);
        flightComboBox.setValue(null);
        bookingDateField.clear();
        seatsField.clear();
        amountField.clear();
        statusField.clear();
    }

    /**
     * Displays an alert dialog with the specified title and message.
     * @param title The title of the dialog
     * @param message The message of the dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation dialog with the specified message.
     * @param message The confirmation message
     * @return true if user clicks OK, false otherwise
     */
    private boolean confirmAction(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
