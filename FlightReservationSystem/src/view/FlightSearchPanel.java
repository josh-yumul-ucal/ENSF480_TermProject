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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Date;

/**
 * View component for flight search.
 * Provides a user interface for searching flights using origin, destination, date and airline criteria.
 * Displays the search results in a table format with flight information.
 */
public class FlightSearchPanel extends BasePane {
    private MainController controller;
    private TextField originField;
    private TextField destinationField;
    private TextField airlineField;
    private TextField dateField;
    private Button searchButton;
    private TableView<Flight> flightTable;
    private ObservableList<Flight> flightData;

    /**
     * Initializes the flight search panel with the controller and UI components.
     * Sets up layout, event handlers, and initial data display.
     * @param controller The main application controller
     */
    public FlightSearchPanel(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    /**
     * Initializes UI components for the flight search panel.
     * Sets up the text fields, buttons, and table with column definitions.
     */
    private void initializeComponents() {
        originField = new TextField();
        originField.setPromptText("Origin");

        destinationField = new TextField();
        destinationField.setPromptText("Destination");

        airlineField = new TextField();
        airlineField.setPromptText("Airline");

        dateField = new TextField();
        dateField.setPromptText("Date (YYYY-MM-DD)");

        searchButton = new Button("Search Flights");

        flightData = FXCollections.observableArrayList();
        flightTable = new TableView<>();

        // Create columns
        TableColumn<Flight, Integer> idColumn = new TableColumn<>("Flight ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().flightIdProperty().asObject());

        TableColumn<Flight, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().flightNumberProperty());

        TableColumn<Flight, String> originColumn = new TableColumn<>("Origin");
        originColumn.setCellValueFactory(cellData -> cellData.getValue().originProperty());

        TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());

        TableColumn<Flight, Date> departureColumn = new TableColumn<>("Departure Time");
        departureColumn.setCellValueFactory(cellData -> cellData.getValue().departureTimeProperty());

        TableColumn<Flight, Date> arrivalColumn = new TableColumn<>("Arrival Time");
        arrivalColumn.setCellValueFactory(cellData -> cellData.getValue().arrivalTimeProperty());

        TableColumn<Flight, String> airlineColumn = new TableColumn<>("Airline");
        airlineColumn.setCellValueFactory(cellData -> cellData.getValue().airlineProperty());

        // Display aircraft name including model
        TableColumn<Flight, String> aircraftColumn = new TableColumn<>("Aircraft");
        aircraftColumn.setCellValueFactory(cellData -> {
            Flight flight = cellData.getValue();
            // Get the aircraft for this flight
            try {
                if (flight.getAircraft() != null) {
                    return new javafx.beans.property.SimpleStringProperty(
                            flight.getAircraft().getAircraftName() + " (" + flight.getAircraft().getModel() + ")"
                    );
                } else {
                    // If we don't have the aircraft object, try to get it from controller
                    var aircraft = controller.getAircraftById(flight.getAircraftId());
                    if (aircraft != null) {
                        return new javafx.beans.property.SimpleStringProperty(
                                aircraft.getAircraftName() + " (" + aircraft.getModel() + ")"
                        );
                    }
                }
            } catch (Exception e) {
                // If there's an error, show "Unknown Aircraft"
                return new javafx.beans.property.SimpleStringProperty("Unknown Aircraft");
            }
            return new javafx.beans.property.SimpleStringProperty("No Aircraft Assigned");
        });

        // Calculate available seats based on total seats minus reserved seats
        TableColumn<Flight, Integer> availableSeatsColumn = new TableColumn<>("Available Seats");
        availableSeatsColumn.setCellValueFactory(cellData -> {
            Flight flight = cellData.getValue();
            int totalSeats = flight.getTotalSeats();

            // Get the reservations for this flight and sum the number of seats
            int reservedSeats = 0;
            try {
                List<model.Reservation> allReservations = controller.getAllReservations();
                for (model.Reservation reservation : allReservations) {
                    if (reservation.getFlightId() == flight.getFlightId()) {
                        reservedSeats += reservation.getNumberOfSeats();
                    }
                }
            } catch (Exception e) {
                // If there's an error, default reserved seats to 0
                reservedSeats = 0;
            }

            return new javafx.beans.property.SimpleIntegerProperty(totalSeats - reservedSeats).asObject();
        });

        TableColumn<Flight, Integer> totalSeatsColumn = new TableColumn<>("Total Seats");
        totalSeatsColumn.setCellValueFactory(cellData -> cellData.getValue().totalSeatsProperty().asObject());

        TableColumn<Flight, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        flightTable.getColumns().addAll(idColumn, numberColumn, originColumn, destinationColumn,
                departureColumn, arrivalColumn, airlineColumn,
                aircraftColumn, availableSeatsColumn, totalSeatsColumn, priceColumn);
        flightTable.setItems(flightData);
    }

    /**
     * Sets up layout structure for the flight search panel.
     * Configures the spacing, padding, and arrangement of UI components.
     */
    private void setupLayout() {
        setPadding(new Insets(10));
        setSpacing(10);

        // Search panel
        HBox searchPanel = new HBox(10);
        searchPanel.getChildren().addAll(
                new Label("Origin:"), originField,
                new Label("Destination:"), destinationField,
                new Label("Airline:"), airlineField,
                new Label("Date (YYYY-MM-DD):"), dateField,
                searchButton
        );

        // Table panel
        ScrollPane tableScrollPane = new ScrollPane(flightTable);
        tableScrollPane.setFitToWidth(true);
        tableScrollPane.setPrefHeight(400);

        VBox resultsBox = new VBox(10);
        resultsBox.getChildren().add(tableScrollPane);

        getChildren().addAll(searchPanel, resultsBox);
    }

    /**
     * Sets up the event handlers for UI interactions.
     * Connects the button clicks to action methods.
     */
    private void setupEventHandlers() {
        searchButton.setOnAction(e -> searchFlights());
    }

    /**
     * Executes the flight search based on user input criteria.
     * Performs validation and calls the controller's search method.
     */
    private void searchFlights() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        String airline = airlineField.getText().trim();
        String dateStr = dateField.getText().trim();

        Date date = null;
        if (!dateStr.isEmpty()) {
            try {
                // Simple date parsing - in real app, use proper date formatter
                date = java.sql.Date.valueOf(dateStr);
            } catch (Exception ex) {
                showAlert("Date Error", "Invalid date format. Use YYYY-MM-DD");
                return;
            }
        }

        // Perform search
        List<Flight> flights = controller.searchFlights(origin, destination, date, airline);

        // Clear existing data
        flightData.clear();

        // Add new data
        flightData.addAll(flights);
    }
}
