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
import model.Flight;
import model.Aircraft;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;

/**
 * View component for flight management.
 * Provides a user interface for adding, updating, and deleting the flight records.
 * Displays the flight schedule in a table format with editable fields and aircraft selection.
 */
public class FlightManagementPanel extends BasePane {
    private MainController controller;

    private TextField flightNumberField;
    private TextField originField;
    private TextField destinationField;
    private TextField airlineField;
    private TextField departureDateField; // Using text field for date input
    private TextField arrivalDateField; // Using text field for date input
    private TextField totalSeatsField;
    private TextField priceField;
    private ComboBox<Aircraft> aircraftComboBox; // Dropdown for aircraft selection
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button refreshButton;
    private TableView<Flight> flightTable;
    private ObservableList<Flight> flightData;

    /**
     * Initializes the flight management panel with the controller and UI components.
     * Sets up layout, event handlers, and initial data display.
     * @param controller The main application controller
     */
    public FlightManagementPanel(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshFlights();
    }

    /**
     * Initializes all UI components for flight management panel.
     * Sets up the text fields, buttons, and table with column definitions.
     */
    private void initializeComponents() {
        flightNumberField = new TextField();
        flightNumberField.setPromptText("Flight Number");

        originField = new TextField();
        originField.setPromptText("Origin");

        destinationField = new TextField();
        destinationField.setPromptText("Destination");

        airlineField = new TextField();
        airlineField.setPromptText("Airline");

        departureDateField = new TextField();
        departureDateField.setPromptText("Departure Date (YYYY-MM-DD HH:MM)");

        arrivalDateField = new TextField();
        arrivalDateField.setPromptText("Arrival Date (YYYY-MM-DD HH:MM)");

        totalSeatsField = new TextField();
        totalSeatsField.setPromptText("Total Seats");

        priceField = new TextField();
        priceField.setPromptText("Price");

        // Create aircraft dropdown
        ObservableList<Aircraft> aircraftList = FXCollections.observableArrayList();
        aircraftComboBox = new ComboBox<>(aircraftList);
        aircraftComboBox.setPromptText("Select Aircraft");

        // Set converter for aircraft combo box
        aircraftComboBox.setConverter(new javafx.util.StringConverter<Aircraft>() {
            @Override
            public String toString(Aircraft aircraft) {
                if (aircraft == null) return "";
                return aircraft.getAircraftName() + " (" + aircraft.getModel() + ")";
            }

            @Override
            public Aircraft fromString(String string) {
                return null; // Not needed for this use case
            }
        });

        aircraftComboBox.setButtonCell(new ListCell<Aircraft>() {
            @Override
            protected void updateItem(Aircraft item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(aircraftComboBox.getPromptText());

                    // Reset the total seats field
                    totalSeatsField.setText("");
                } else {
                    setText(item.getAircraftName() + " (" + item.getModel() + ")");
                }
            }
        });

        addButton = new Button("Add Flight");
        updateButton = new Button("Update Flight");
        deleteButton = new Button("Delete Flight");
        refreshButton = new Button("Refresh");

        flightData = FXCollections.observableArrayList();
        flightTable = new TableView<>();

        // Create columns
        TableColumn<Flight, String> numberColumn = new TableColumn<>("Flight Number");
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().flightNumberProperty());

        TableColumn<Flight, String> originColumn = new TableColumn<>("Origin");
        originColumn.setCellValueFactory(cellData -> cellData.getValue().originProperty());

        TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());

        TableColumn<Flight, String> airlineColumn = new TableColumn<>("Airline");
        airlineColumn.setCellValueFactory(cellData -> cellData.getValue().airlineProperty());

        TableColumn<Flight, Date> departureColumn = new TableColumn<>("Departure Time");
        departureColumn.setCellValueFactory(cellData -> cellData.getValue().departureTimeProperty());

        TableColumn<Flight, Date> arrivalColumn = new TableColumn<>("Arrival Time");
        arrivalColumn.setCellValueFactory(cellData -> cellData.getValue().arrivalTimeProperty());

        TableColumn<Flight, String> aircraftIdColumn = new TableColumn<>("Aircraft");
        aircraftIdColumn.setCellValueFactory(cellData -> {
            Flight flight = cellData.getValue();
            Aircraft aircraft = null;

            // Find the aircraft associated with this flight
            for (Aircraft a : controller.getAllAircraft()) {
                if (a.getAircraftId() == flight.getAircraftId()) {
                    aircraft = a;
                    break;
                }
            }

            if (aircraft != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        aircraft.getAircraftName() + " (" + aircraft.getModel() + ")"
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty("Unknown Aircraft (" + flight.getAircraftId() + ")");
            }
        });

        TableColumn<Flight, Integer> totalSeatsColumn = new TableColumn<>("Total Seats");
        totalSeatsColumn.setCellValueFactory(cellData -> cellData.getValue().totalSeatsProperty().asObject());

        TableColumn<Flight, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        priceColumn.setCellFactory(column -> new TableCell<Flight, Double>() {
            private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Format as currency with exactly 2 decimal places
                    setText(currencyFormatter.format(item));
                }
            }
        });

        flightTable.getColumns().addAll(numberColumn, originColumn, destinationColumn,
                airlineColumn,
                departureColumn, arrivalColumn, aircraftIdColumn,
                totalSeatsColumn, priceColumn);
        flightTable.setItems(flightData);
    }

    /**
     * Sets up layout structure for the flight management panel.
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

        inputPanel.add(new Label("Flight Number:"), 0, 0);
        inputPanel.add(flightNumberField, 1, 0);

        inputPanel.add(new Label("Origin:"), 0, 1);
        inputPanel.add(originField, 1, 1);

        inputPanel.add(new Label("Destination:"), 0, 2);
        inputPanel.add(destinationField, 1, 2);

        inputPanel.add(new Label("Airline:"), 0, 3);
        inputPanel.add(airlineField, 1, 3);

        inputPanel.add(new Label("Departure Date (YYYY-MM-DD HH:MM):"), 0, 4);
        inputPanel.add(departureDateField, 1, 4);

        inputPanel.add(new Label("Arrival Date (YYYY-MM-DD HH:MM):"), 0, 5);
        inputPanel.add(arrivalDateField, 1, 5);

        inputPanel.add(new Label("Total Seats:"), 0, 6);
        inputPanel.add(totalSeatsField, 1, 6);

        inputPanel.add(new Label("Price ($):"), 0, 7);
        inputPanel.add(priceField, 1, 7);

        inputPanel.add(new Label("Aircraft:"), 0, 8);
        inputPanel.add(aircraftComboBox, 1, 8);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton);

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(inputPanel, buttonBox);

        // Results panel
        ScrollPane scrollPane = new ScrollPane(flightTable);
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
        addButton.setOnAction(e -> addFlight());
        updateButton.setOnAction(e -> updateFlight());
        deleteButton.setOnAction(e -> deleteFlight());
        refreshButton.setOnAction(e -> refreshFlights());

        flightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadFlightData(newSelection);
            }
        });

        // Add listener to the aircraft combo box to pre-populate the total seats field
        aircraftComboBox.valueProperty().addListener((obs, oldAircraft, newAircraft) -> {
            if (newAircraft != null) {
                totalSeatsField.setText(String.valueOf(newAircraft.getCapacity()));
            }
        });

        // Format the price field when the value changes
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    double value = Double.parseDouble(newValue);

                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    String formatted = df.format(value);

                    if (!formatted.equals(priceField.getText())) {
                        priceField.setText(formatted);
                    }
                } catch (NumberFormatException e) {
                    // If the price is not a valid number, keep as is
                }
            }
        });
    }

    /**
     * Adds a new flight record.
     * Validates the input and refreshes the table display after successful creation.
     */
    private void addFlight() {
        Flight flight = createFlightFromInput();
        if (flight != null) {
            if (!validateArrivalTime(flight)) {
                return;
            }

            if (controller.addFlight(flight)) {
                showAlert("Success", "Flight added successfully");
                refreshFlights();
                clearInputFields();
            } else {
                showAlert("Error", "Failed to add flight");
            }
        }
    }

    /**
     * Updates an existing flight record.
     * Uses selected item reference to preserve ID during the update operation.
     */
    private void updateFlight() {
        Flight selected = flightTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a flight to update");
            return;
        }

        Flight flight = createFlightFromInput();
        if (flight != null) {
            if (!validateArrivalTime(flight)) {
                return;
            }

            // Preserve the ID from the original selection
            flight.setFlightId(selected.getFlightId());

            if (controller.updateFlight(flight)) {
                showAlert("Success", "Flight updated successfully");
                refreshFlights();
            } else {
                showAlert("Error", "Failed to update flight");
            }
        }
    }

    /**
     * Deletes a flight record.
     * Prompts for user confirmation before proceeding with the deletion.
     */
    private void deleteFlight() {
        Flight selected = flightTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a flight to delete");
            return;
        }

        if (confirmAction("Are you sure you want to delete this flight?")) {
            if (controller.deleteFlight(selected.getFlightId())) { // Use actual ID
                showAlert("Success", "Flight deleted successfully");
                refreshFlights();
            } else {
                showAlert("Error", "Failed to delete flight");
            }
        }
    }

    /**
     * Validates that the arrival time is after the departure time.
     * @param flight The flight to validate
     * @return true If the validation passes, false otherwise
     */
    private boolean validateArrivalTime(Flight flight) {
        Date departureTime = flight.getDepartureTime();
        Date arrivalTime = flight.getArrivalTime();

        if (departureTime != null && arrivalTime != null) {
            if (arrivalTime.before(departureTime)) {
                showAlert("Time Error", "Arrival time must be after departure time");
                return false;
            }
        }

        return true;
    }

    /**
     * Refreshes the flight table.
     * This ensures that auto-generated IDs are properly loaded after the record has been created.
     */
    private void refreshFlights() {
        List<Flight> flights = controller.getAllFlights();
        flightData.clear();
        flightData.addAll(flights);

        // Also populate aircraft dropdown
        ObservableList<Aircraft> aircraftList = FXCollections.observableArrayList();
        for (Aircraft aircraft : controller.getAllAircraft()) {
            aircraftList.add(aircraft);
        }
        aircraftComboBox.setItems(aircraftList);
        aircraftComboBox.setValue(null);
    }

    /**
     * Creates Flight object from user input fields.
     * Validates the required fields and parses date/time values.
     * @return Flight object if validation passes, null otherwise
     */
    private Flight createFlightFromInput() {
        String flightNumber = flightNumberField.getText().trim();
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        String airline = airlineField.getText().trim();
        String departureDateStr = departureDateField.getText().trim();
        String arrivalDateStr = arrivalDateField.getText().trim();
        String totalSeatsStr = totalSeatsField.getText().trim();
        String priceStr = priceField.getText().trim();
        Aircraft selectedAircraft = aircraftComboBox.getValue();

        if (flightNumber.isEmpty() || origin.isEmpty() || destination.isEmpty() ||
                departureDateStr.isEmpty() || arrivalDateStr.isEmpty() ||
                totalSeatsStr.isEmpty() || priceStr.isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return null;
        }

        try {
            int totalSeats = Integer.parseInt(totalSeatsStr);

            double price = Double.parseDouble(priceStr.replaceAll("[^0-9.]", ""));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date departureDate = sdf.parse(departureDateStr);
            Date arrivalDate = sdf.parse(arrivalDateStr);

            Flight flight = new Flight();
            flight.setFlightNumber(flightNumber);
            flight.setOrigin(origin);
            flight.setDestination(destination);
            flight.setAirline(airline);
            flight.setDepartureTime(departureDate);
            flight.setArrivalTime(arrivalDate);
            flight.setTotalSeats(totalSeats);
            flight.setPrice(price);

            // Set aircraft if selected
            if (selectedAircraft != null) {
                flight.setAircraftId(selectedAircraft.getAircraftId());
                flight.setAircraft(selectedAircraft);
            }

            return flight;
        } catch (Exception e) {
            showAlert("Error", "Please enter valid numbers and a valid date in YYYY-MM-DD HH:MM format");
            return null;
        }
    }

    /**
     * Loads data from Flight object into the input fields.
     * Used when selecting an item to update its information.
     * @param flight The Flight object to load data from
     */
    private void loadFlightData(Flight flight) {
        flightNumberField.setText(flight.getFlightNumber());
        originField.setText(flight.getOrigin());
        destinationField.setText(flight.getDestination());
        airlineField.setText(flight.getAirline());
        departureDateField.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(flight.getDepartureTime()));
        arrivalDateField.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(flight.getArrivalTime()));
        totalSeatsField.setText(String.valueOf(flight.getTotalSeats()));

        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        String formattedPrice = df.format(flight.getPrice());
        priceField.setText(formattedPrice);

        // Set aircraft in dropdown if exists
        if (flight.getAircraft() != null) {
            // Find the matching aircraft in the combo box and select it
            Aircraft selectedAircraft = flight.getAircraft();
            for (Aircraft aircraft : aircraftComboBox.getItems()) {
                if (aircraft.getAircraftId() == selectedAircraft.getAircraftId()) {
                    aircraftComboBox.setValue(aircraft);
                    break;
                }
            }
        } else {
            // If we have just the aircraft ID but not the full object, try to find it
            int aircraftId = flight.getAircraftId();
            if (aircraftId > 0) {
                for (Aircraft aircraft : aircraftComboBox.getItems()) {
                    if (aircraft.getAircraftId() == aircraftId) {
                        aircraftComboBox.setValue(aircraft);
                        break;
                    }
                }
            } else {
                aircraftComboBox.setValue(null);
            }
        }
    }

    /**
     * Clears all of the input fields in the form.
     * Used after the successful creation or when resetting the form.
     */
    private void clearInputFields() {
        flightNumberField.clear();
        originField.clear();
        destinationField.clear();
        airlineField.clear();
        departureDateField.clear();
        arrivalDateField.clear();
        totalSeatsField.clear();
        priceField.clear();
        aircraftComboBox.setValue(null);
    }
}
