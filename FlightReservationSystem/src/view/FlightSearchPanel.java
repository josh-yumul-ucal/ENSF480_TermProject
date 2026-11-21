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
 * Provides a user interface for searching flights using origin, destination, and date criteria.
 * Displays the search results in a table format with flight information.
 */
public class FlightSearchPanel extends VBox {
    private MainController controller;
    private TextField originField;
    private TextField destinationField;
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

        TableColumn<Flight, Integer> totalSeatsColumn = new TableColumn<>("Total Seats");
        totalSeatsColumn.setCellValueFactory(cellData -> cellData.getValue().totalSeatsProperty().asObject());

        TableColumn<Flight, Integer> availableSeatsColumn = new TableColumn<>("Available Seats");
        availableSeatsColumn.setCellValueFactory(cellData -> cellData.getValue().availableSeatsProperty().asObject());

        TableColumn<Flight, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        flightTable.getColumns().addAll(idColumn, numberColumn, originColumn, destinationColumn,
                departureColumn, arrivalColumn, airlineColumn,
                totalSeatsColumn, availableSeatsColumn, priceColumn);
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
        List<Flight> flights = controller.searchFlights(origin, destination, date);

        // Clear existing data
        flightData.clear();

        // Add new data
        flightData.addAll(flights);
    }

    /**
     * Displays an alert dialog with the specified title and message.
     * Used for user feedback during error conditions or during success notifications.
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
}
