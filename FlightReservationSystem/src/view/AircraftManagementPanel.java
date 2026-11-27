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
import model.Aircraft;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/**
 * User interface view component for adding, updating and deleting aircraft records.
 * Displays aircraft in a table format with editable fields.
 */
public class AircraftManagementPanel extends BasePane {
    private MainController controller;

    private TextField aircraftNameField;
    private TextField modelField;
    private TextField capacityField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button clearButton;
    private Button refreshButton;
    private TableView<Aircraft> aircraftTable;
    private ObservableList<Aircraft> aircraftData;

    /**
     * Initializes aircraft management panel with controller and UI components.
     * Sets up layout, event handlers, and initial data display.
     * @param controller The main application controller
     */
    public AircraftManagementPanel(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshAircraft();
    }

    /**
     * Initializes the UI components for the aircraft management panel.
     * Sets up text fields, buttons, and table with column definitions.
     */
    private void initializeComponents() {
        aircraftNameField = new TextField();
        aircraftNameField.setPromptText("Aircraft Name");

        modelField = new TextField();
        modelField.setPromptText("Model");

        capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        addButton = new Button("Add Aircraft");
        updateButton = new Button("Update Aircraft");
        deleteButton = new Button("Delete Aircraft");
        clearButton = new Button("Clear");
        refreshButton = new Button("Refresh");

        aircraftData = FXCollections.observableArrayList();
        aircraftTable = new TableView<>();

        // Create columns
        TableColumn<Aircraft, String> nameColumn = new TableColumn<>("Aircraft Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().aircraftNameProperty());

        TableColumn<Aircraft, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());

        TableColumn<Aircraft, Integer> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(cellData -> cellData.getValue().capacityProperty().asObject());

        aircraftTable.getColumns().addAll(nameColumn, modelColumn, capacityColumn);
        aircraftTable.setItems(aircraftData);
    }

    /**
     * Sets up the layout structure for aircraft management panel.
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

        inputPanel.add(new Label("Aircraft Name:"), 0, 0);
        inputPanel.add(aircraftNameField, 1, 0);

        inputPanel.add(new Label("Model:"), 0, 1);
        inputPanel.add(modelField, 1, 1);

        inputPanel.add(new Label("Capacity:"), 0, 2);
        inputPanel.add(capacityField, 1, 2);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton, refreshButton);

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(inputPanel, buttonBox);

        // Results panel
        ScrollPane scrollPane = new ScrollPane(aircraftTable);
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
        addButton.setOnAction(e -> addAircraft());
        updateButton.setOnAction(e -> updateAircraft());
        deleteButton.setOnAction(e -> deleteAircraft());
        clearButton.setOnAction((e -> clearInputFields()));
        refreshButton.setOnAction(e -> refreshAircraft());

        aircraftTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadAircraftData(newSelection);
            }
        });
    }

    /**
     * Adds a new aircraft record.
     * Validates the input and refreshes the table display after successful creation.
     */
    private void addAircraft() {
        Aircraft aircraft = createAircraftFromInput();
        if (aircraft != null) {
            if (controller.addAircraft(aircraft)) {
                showAlert("Success", "Aircraft added successfully");
                refreshAircraft();
                clearInputFields();
            } else {
                showAlert("Error", "Failed to add aircraft");
            }
        }
    }

    /**
     * Updates an existing aircraft record.
     * Uses selected item reference to preserve ID during the update operation.
     */
    private void updateAircraft() {
        Aircraft selected = aircraftTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select an aircraft to update");
            return;
        }

        Aircraft aircraft = createAircraftFromInput();
        if (aircraft != null) {
            // Preserve the ID from the original selection
            aircraft.setAircraftId(selected.getAircraftId());

            if (controller.updateAircraft(aircraft)) {
                showAlert("Success", "Aircraft updated successfully");
                refreshAircraft();
            } else {
                showAlert("Error", "Failed to update aircraft");
            }
        }
    }

    /**
     * Deletes an aircraft record.
     * Prompts user confirmation before proceeding with the deletion.
     */
    private void deleteAircraft() {
        Aircraft selected = aircraftTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select an aircraft to delete");
            return;
        }

        if (confirmAction("Are you sure you want to delete this aircraft?")) {
            if (controller.deleteAircraft(selected.getAircraftId())) { // Use actual ID
                showAlert("Success", "Aircraft deleted successfully");
                refreshAircraft();
            } else {
                showAlert("Error", "Failed to delete aircraft");
            }
        }
    }

    /**
     * Refreshes the aircraft table with the current data from the database.
     * This ensures that auto-generated IDs are properly loaded after record hsa been created.
     */
    private void refreshAircraft() {
        List<Aircraft> aircraftList = controller.getAllAircraft();
        aircraftData.clear();
        aircraftData.addAll(aircraftList);
    }

    /**
     * Creates an Aircraft object from the user input fields.
     * Validates required fields and parses the numeric values.
     * @return Aircraft object if the validation passes, null otherwise
     */
    private Aircraft createAircraftFromInput() {
        String aircraftName = aircraftNameField.getText().trim();
        String model = modelField.getText().trim();
        String capacityStr = capacityField.getText().trim();

        if (aircraftName.isEmpty() || model.isEmpty() || capacityStr.isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return null;
        }

        try {
            int capacity = Integer.parseInt(capacityStr);

            Aircraft aircraft = new Aircraft();
            aircraft.setAircraftName(aircraftName);
            aircraft.setModel(model);
            aircraft.setCapacity(capacity);

            return aircraft;
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for capacity");
            return null;
        }
    }

    /**
     * Loads data from Aircraft object into the input fields.
     * Used when selecting an item to update its information.
     * @param aircraft The Aircraft object that is used to load data from
     */
    private void loadAircraftData(Aircraft aircraft) {
        aircraftNameField.setText(aircraft.getAircraftName());
        modelField.setText(aircraft.getModel());
        capacityField.setText(String.valueOf(aircraft.getCapacity()));
    }

    /**
     * Clears all of the input fields in the form.
     * Used after the successful creation or when resetting the form.
     */
    private void clearInputFields() {
        aircraftNameField.clear();
        modelField.clear();
        capacityField.clear();
    }
}
