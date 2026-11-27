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
import java.util.Random;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * View component for reservation management.
 * Provides a user interface for viewing, adding, updating, and deleting the reservations.
 * Displays the reservation information in a table format with booking information.
 */
public class ReservationManagementPanel extends BasePane {
    private MainController controller;
    private User currentUser;
    private Random random = new Random();

    private ComboBox<User> customerIdComboBox;
    private ComboBox<Flight> flightComboBox;
    private TextField bookingDateField;
    private TextField seatsField;
    private TextField amountField;
    private ComboBox<String> paymentMethodComboBox;
    private TextField cardholderNameField;
    private TextField creditCardNumberField;
    private TextField expiryDateField;
    private TextField cvvField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button refreshButton;
    private Button generateConfirmationButton;
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

        customerIdComboBox.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(customerIdComboBox.getPromptText());
                } else {
                    setText(item.getFirstName() + " " + item.getLastName() + " (" + item.getUserId() + ")");
                }
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

        flightComboBox.setButtonCell(new ListCell<Flight>() {
            @Override
            protected void updateItem(Flight item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(flightComboBox.getPromptText());
                } else {
                    setText(item.getFlightNumber() + " (" + item.getOrigin() + " to " + item.getDestination() + ")");
                }
            }
        });

        bookingDateField = new TextField();
        bookingDateField.setPromptText("Booking Date (YYYY-MM-DD HH:MM)");

        seatsField = new TextField();
        seatsField.setPromptText("Seats");

        amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setEditable(false);
        amountField.setStyle("-fx-background-color: #e0e0e0;");

        // Create a payment method dropdown with "VISA" and "Mastercard" options
        ObservableList<String> paymentMethods = FXCollections.observableArrayList(
                "VISA",
                "Mastercard"
        );
        paymentMethodComboBox = new ComboBox<>(paymentMethods);
        paymentMethodComboBox.setPromptText("Select Payment Method");
        paymentMethodComboBox.getSelectionModel().selectFirst();

        cardholderNameField = new TextField();
        cardholderNameField.setPromptText("Cardholder Name");

        creditCardNumberField = new TextField();
        creditCardNumberField.setPromptText("Credit Card Number");

        expiryDateField = new TextField();
        expiryDateField.setPromptText("Expiry YY/MM");

        cvvField = new TextField();
        cvvField.setPromptText("CVV");

        addButton = new Button("Add Reservation");
        updateButton = new Button("Update Reservation");
        deleteButton = new Button("Delete Reservation");
        refreshButton = new Button("Refresh");
        generateConfirmationButton = new Button("Generate Confirmation");

        reservationData = FXCollections.observableArrayList();
        reservationTable = new TableView<>();

        // Create columns
        TableColumn<Reservation, String> reservationIdColumn = new TableColumn<>("Reservation ID");
        reservationIdColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            // Generate a reservation reference by adding the reference ID to 1000000
            int displayId = reservation.getReservationId() + 1000000;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(displayId));
        });

        TableColumn<Reservation, String> customerIdColumn = new TableColumn<>("Customer");
        customerIdColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            User customer = null;

            // Find the customer associated with this reservation
            for (User user : controller.getAllUsers()) {
                if (user.getUserId() == reservation.getCustomerId()) {
                    customer = user;
                    break;
                }
            }

            if (customer != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        customer.getFirstName() + " " + customer.getLastName() + " (" + customer.getUserId() + ")"
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty("Unknown Customer (" + reservation.getCustomerId() + ")");
            }
        });

        TableColumn<Reservation, String> flightIdColumn = new TableColumn<>("Flight");
        flightIdColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            Flight flight = null;

            // Find the flight associated with this reservation
            for (Flight f : controller.getAllFlights()) {
                if (f.getFlightId() == reservation.getFlightId()) {
                    flight = f;
                    break;
                }
            }

            if (flight != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        flight.getFlightNumber() + " (" + flight.getOrigin() + " to " + flight.getDestination() + ")"
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty("Unknown Flight (" + reservation.getFlightId() + ")");
            }
        });

        TableColumn<Reservation, Date> bookingDateColumn = new TableColumn<>("Booking Date");
        bookingDateColumn.setCellValueFactory(cellData -> cellData.getValue().bookingDateProperty());

        TableColumn<Reservation, Integer> seatsColumn = new TableColumn<>("Seats");
        seatsColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfSeatsProperty().asObject());

        TableColumn<Reservation, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());

        amountColumn.setCellFactory(column -> new TableCell<Reservation, Double>() {
            private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormatter.format(item));
                }
            }
        });

        reservationTable.getColumns().addAll(reservationIdColumn, customerIdColumn, flightIdColumn, bookingDateColumn,
                seatsColumn, amountColumn);
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

        inputPanel.add(new Label("Amount ($):"), 0, 4);
        inputPanel.add(amountField, 1, 4);
        inputPanel.add(new Label("Payment Method:"), 2, 0);
        inputPanel.add(paymentMethodComboBox, 3, 0);

        inputPanel.add(new Label("Cardholder Name:"), 2, 1);
        inputPanel.add(cardholderNameField, 3, 1);
        inputPanel.add(new Label("Credit Card #:"), 2, 2);
        inputPanel.add(creditCardNumberField, 3, 2);

        inputPanel.add(new Label("Expiry YY/MM:"), 2, 3);
        inputPanel.add(expiryDateField, 3, 3);
        inputPanel.add(new Label("CVV:"), 2, 4);
        inputPanel.add(cvvField, 3, 4);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton, generateConfirmationButton);

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
        generateConfirmationButton.setOnAction(e -> generateConfirmation());

        reservationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadReservationData(newSelection);
            }
        });

        // Add listeners to input fields to enable/disable the confirmation button
        customerIdComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateConfirmationButtonState());
        flightComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateConfirmationButtonState());
        bookingDateField.textProperty().addListener((obs, oldVal, newVal) -> updateConfirmationButtonState());
        seatsField.textProperty().addListener((obs, oldVal, newVal) -> updateConfirmationButtonState());
        paymentMethodComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateConfirmationButtonState());

        // Add a listener to the seats field to validate the value and calculate the amount
        seatsField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty() && flightComboBox.getValue() != null) {
                calculateAmount();
                validateSeatAvailability();
            }
        });

        // Add a listener to the flight selection to calculate the amount when flight changes
        flightComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !seatsField.getText().trim().isEmpty()) {
                calculateAmount();
                validateSeatAvailability();
            }
        });

        // Format the amount field when the value changes
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    double value = Double.parseDouble(newValue);
                    // Format with exactly 2 decimal places, keeping trailing zeros
                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    String formatted = df.format(value);

                    if (!formatted.equals(amountField.getText())) {
                        amountField.setText(formatted);
                    }
                } catch (NumberFormatException e) {
                    // If the amount is not a valid number, keep as is
                }
            }
        });

        // Format and validate the credit card field
        creditCardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                formatCreditCardNumber(newValue);
            }
        });

        // Validate the expiry date
        expiryDateField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // When focus is lost
                String expiryDate = expiryDateField.getText().trim();
                if (!expiryDate.isEmpty()) {
                    validateExpiryDate(expiryDate);
                }
            }
        });

        // Validate the credit card CVV
        cvvField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // When focus is lost
                String cvv = cvvField.getText().trim();
                if (!cvv.isEmpty()) {
                    validateCvv(cvv);
                }
            }
        });
    }

    /**
     * Format the credit card number with spaces after every 4 digits
     */
    private void formatCreditCardNumber(String value) {
        // Remove all non-digit characters
        String digitsOnly = value.replaceAll("[^0-9]", "");

        // There needs to be at least one digit
        if (digitsOnly.length() > 0) {
            StringBuilder formatted = new StringBuilder();

            // Format with 4 digits followed by a space, except for the last group
            for (int i = 0; i < digitsOnly.length(); i++) {
                if (i > 0 && i % 4 == 0) {
                    formatted.append(" ");
                }
                formatted.append(digitsOnly.charAt(i));
            }

            // Only update if the formatted value is different from the current value
            if (!formatted.toString().equals(creditCardNumberField.getText())) {
                creditCardNumberField.setText(formatted.toString());

                // Move cursor to end of input
                creditCardNumberField.positionCaret(formatted.length());
            }
        }
    }

    /**
     * Validate to ensure the expiry date has not expired and is in correct format (YY/MM)
     */
    private boolean validateExpiryDate(String expiryDate) {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return true;
        }

        // Expiry format should be YY/MM
        if (!expiryDate.matches("^\\d{2}/\\d{2}$")) {
            showAlert("Validation Error", "Expiry date must be in YY/MM format");
            return false;
        }

        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[0]);

            // Validate the month
            if (month < 1 || month > 12) {
                showAlert("Validation Error", "Invalid month in expiry date");
                return false;
            }

            // Get the current date for comparison
            LocalDate currentDate = LocalDate.now();
            int currentYear = currentDate.getYear() % 100; // Get last two digits
            int currentMonth = currentDate.getMonthValue();

            // Check if the card is expired (assuming year 2000+)
            int fullYear = 2000 + year;
            LocalDate expiryDateParsed = LocalDate.of(fullYear, month, 1);
            LocalDate today = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), 1);

            // The card has expired if the expiry date is before or equal to today
            if (expiryDateParsed.isBefore(today) || expiryDateParsed.isEqual(today)) {
                showAlert("Validation Error", "Credit card has expired");
                return false;
            }

            return true;
        } catch (Exception e) {
            showAlert("Validation Error", "Invalid expiry date format");
            return false;
        }
    }

    /**
     * Validates that the CVV is a 3-digit numeric value
     */
    private boolean validateCvv(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            return true;
        }

        // Check if the CVV is three digits
        if (!cvv.matches("^\\d{3}$")) {
            showAlert("Validation Error", "CVV must be a 3-digit numeric value");
            return false;
        }

        return true;
    }

    /**
     * Updates the enabled state of the Generate Confirmation button based on input fields
     */
    private void updateConfirmationButtonState() {
        boolean hasData = customerIdComboBox.getValue() != null &&
                flightComboBox.getValue() != null &&
                !bookingDateField.getText().trim().isEmpty() &&
                !seatsField.getText().trim().isEmpty() &&
                paymentMethodComboBox.getValue() != null;

        generateConfirmationButton.setDisable(!hasData);
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
        // Validate that the booking date is not after flight departure date
        if (!validateBookingDate()) {
            return;
        }

        // Validate seats are available before proceeding
        if (!validateSeatAvailability()) {
            return;
        }

        // Validate the credit card information
        if (!validateCreditCardInformation()) {
            return;
        }

        Reservation reservation = createReservationFromInput();
        if (reservation != null) {
            if (controller.addReservation(reservation)) {
                showAlert("Success", "Reservation added successfully");
                refreshReservations();
                clearInputFields();
                updateConfirmationButtonState();
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
        // Validate that the booking date is not after flight departure date
        if (!validateBookingDate()) {
            return;
        }

        // Validate seats are available before proceeding
        if (!validateSeatAvailability()) {
            return;
        }

        // Validate the credit card information
        if (!validateCreditCardInformation()) {
            return;
        }

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
                updateConfirmationButtonState();
            } else {
                showAlert("Error", "Failed to update reservation");
            }
        }
    }

    /**
     * Validates the credit card information
     */
    private boolean validateCreditCardInformation() {
        String creditCardNumber = creditCardNumberField.getText().trim();
        String expiryDate = expiryDateField.getText().trim();
        String cvv = cvvField.getText().trim();

        // Make sure the credit card number is not empty
        if (creditCardNumber.isEmpty()) {
            showAlert("Validation Error", "Credit card number is required");
            return false;
        }

        // Remove spaces and then check if it's 16 digits
        String digitsOnly = creditCardNumber.replaceAll("[^0-9]", "");
        if (digitsOnly.length() != 16) {
            showAlert("Validation Error", "Credit card number must contain exactly 16 digits");
            return false;
        }

        // Validate the credit card expiry date
        if (!validateExpiryDate(expiryDate)) {
            return false;
        }

        // Validate the CVV
        if (!validateCvv(cvv)) {
            return false;
        }

        return true;
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
                updateConfirmationButtonState();
            } else {
                showAlert("Error", "Failed to delete reservation");
            }
        }
    }

    /**
     * Validates that the booking date is not after flight departure date
     * @return true If the validation passes, false otherwise
     */
    private boolean validateBookingDate() {
        Flight selectedFlight = flightComboBox.getValue();
        String bookingDateStr = bookingDateField.getText().trim();

        if (selectedFlight == null || bookingDateStr.isEmpty()) {
            return true;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date bookingDate = sdf.parse(bookingDateStr);
            Date departureDate = selectedFlight.getDepartureTime();

            if (bookingDate.after(departureDate)) {
                showAlert("Date Error", "Booking date cannot be after flight departure date!");
                return false;
            }

            return true;
        } catch (Exception e) {
            showAlert("Date Error", "Invalid date format. Use YYYY-MM-DD HH:MM");
            return false;
        }
    }

    /**
     * Generates a confirmation report based on the current input fields or selected reservation
     */
    private void generateConfirmation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        // If there's a selected reservation, use that one
        if (selectedReservation != null) {
            showConfirmationReport(selectedReservation);
            return;
        }

        // Otherwise try to create a reservation from input fields
        Reservation reservation = createReservationFromInput();
        if (reservation != null) {
            // For new reservations, we can't show the actual ID since it hasn't been saved yet
            // So we'll show "New Reservation - ID will be generated after saving"
            reservation.setReservationId(0);
            showConfirmationReport(reservation);
        } else {
            showAlert("Error", "No reservation data available to generate confirmation");
        }
    }

    /**
     * Displays a confirmation report in a new window
     */
    private void showConfirmationReport(Reservation reservation) {
        // Create the report content
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("=====================================\n");
        reportContent.append("         RESERVATION CONFIRMATION\n");
        reportContent.append("=====================================\n\n");

        // Get customer information
        User customer = null;
        for (User user : controller.getAllUsers()) {
            if (user.getUserId() == reservation.getCustomerId()) {
                customer = user;
                break;
            }
        }

        // Get flight information
        Flight flight = null;
        for (Flight f : controller.getAllFlights()) {
            if (f.getFlightId() == reservation.getFlightId()) {
                flight = f;
                break;
            }
        }

        // Handle the case where we're showing a new reservation that hasn't been saved yet
        int displayReservationId = 0;
        if (reservation.getReservationId() == 0) {
            reportContent.append("Reservation ID: NEW - Will be generated after saving\n");
        } else {
            displayReservationId = reservation.getReservationId() + 1000000;
            reportContent.append("Reservation ID: ").append(displayReservationId).append("\n");
        }

        reportContent.append("Customer: ").append(customer != null ? customer.getFirstName() + " " + customer.getLastName() : "Unknown").append("\n");
        reportContent.append("Flight Number: ").append(flight != null ? flight.getFlightNumber() : "Unknown").append("\n");
        reportContent.append("Origin: ").append(flight != null ? flight.getOrigin() : "Unknown").append("\n");
        reportContent.append("Destination: ").append(flight != null ? flight.getDestination() : "Unknown").append("\n");
        reportContent.append("Departure Date: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(reservation.getBookingDate())).append("\n");
        reportContent.append("Number of Seats: ").append(reservation.getNumberOfSeats()).append("\n");
        reportContent.append("Total Amount: ").append(NumberFormat.getCurrencyInstance().format(reservation.getTotalAmount())).append("\n");
        reportContent.append("Payment Method: ").append(reservation.getPaymentMethod()).append("\n");
        reportContent.append("=====================================\n");

        // Create the report window
        Stage reportStage = new Stage();
        reportStage.setTitle("Reservation Confirmation Report");

        TextArea textArea = new TextArea(reportContent.toString());
        textArea.setEditable(false);
        textArea.setPrefSize(500, 400);

        VBox reportBox = new VBox(10);
        reportBox.setPadding(new Insets(20));
        reportBox.getChildren().add(textArea);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> reportStage.close());
        reportBox.getChildren().add(closeButton);

        Scene scene = new Scene(reportBox, 600, 500);
        reportStage.setScene(scene);
        reportStage.show();
    }

    /**
     * Validates that there are enough available seats on the flight for the reservation
     * @return true If there are enough seats, false otherwise
     */
    private boolean validateSeatAvailability() {
        // Get flight and seat count from input fields
        Flight selectedFlight = flightComboBox.getValue();
        String seatsStr = seatsField.getText().trim();

        if (selectedFlight == null || seatsStr.isEmpty()) {
            return true; // No validation needed if no flight or seats specified
        }

        try {
            int requestedSeats = Integer.parseInt(seatsStr);

            // Get current reserved seats for this flight
            int reservedSeats = controller.getReservationsByFlightId(selectedFlight.getFlightId());

            // Calculate available seats (total - reserved)
            int availableSeats = selectedFlight.getTotalSeats() - reservedSeats;

            // Check if we have enough seats
            if (requestedSeats > availableSeats) {
                showAlert("Seat Availability Error",
                        "Not enough seats available!\n" +
                                "Available: " + availableSeats + "\n" +
                                "Requested: " + requestedSeats);
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid number for seats");
            return false;
        }
    }

    /**
     * Calculates the total amount using the flight price and the number of seats
     */
    private void calculateAmount() {
        Flight selectedFlight = flightComboBox.getValue();
        String seatsStr = seatsField.getText().trim();

        if (selectedFlight != null && !seatsStr.isEmpty()) {
            try {
                int numberOfSeats = Integer.parseInt(seatsStr);
                double totalAmount = selectedFlight.getPrice() * numberOfSeats;
                // Format with exactly 2 decimal places, keeping trailing zeros
                java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                amountField.setText(df.format(totalAmount));
            } catch (NumberFormatException e) {
                // Do nothing since invalid input will be handled elsewhere
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
        String paymentMethod = paymentMethodComboBox.getValue();
        String cardholderName = cardholderNameField.getText().trim();
        String creditCardNumber = creditCardNumberField.getText().trim();
        String expiryDate = expiryDateField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (selectedUser == null || selectedFlight == null || bookingDateStr.isEmpty() ||
                seatsStr.isEmpty() || amountStr.isEmpty()) {
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
            reservation.setPaymentMethod(paymentMethod);
            reservation.setCardholderName(cardholderName);
            reservation.setCreditCardNumber(creditCardNumber);
            reservation.setExpiryDate(expiryDate);
            reservation.setCvv(cvv);
            // Note: transactionId is not set here, it is generated by the database

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

        // Format amount with exactly 2 decimal places
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        String formattedAmount = df.format(reservation.getTotalAmount());
        amountField.setText(formattedAmount);

        paymentMethodComboBox.setValue(reservation.getPaymentMethod());
        cardholderNameField.setText(reservation.getCardholderName());
        creditCardNumberField.setText(reservation.getCreditCardNumber());
        expiryDateField.setText(reservation.getExpiryDate());
        cvvField.setText(reservation.getCvv());
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
        paymentMethodComboBox.getSelectionModel().selectFirst();
        cardholderNameField.clear();
        creditCardNumberField.clear();
        expiryDateField.clear();
        cvvField.clear();
        updateConfirmationButtonState();
    }
}
