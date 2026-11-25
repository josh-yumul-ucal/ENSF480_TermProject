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
import model.Promotion;
import model.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * View component for promotion news management.
 * Provides a user interface for viewing promotional news announcements.
 */
public class PromotionPanel extends BasePane {
    private MainController controller;
    private User currentUser;
    private boolean isEditable;

    private DatePicker dateField;
    private TextArea textArea;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button refreshButton;
    private TableView<Promotion> promotionTable;
    private ObservableList<Promotion> promotionData;

    /**
     * Initializes the promotion panel with the controller and UI components.
     * Sets up layout, event handlers, and initial data display.
     * @param controller The main application controller
     */
    public PromotionPanel(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshPromotions();
        updateButtonState();
    }

    /**
     * Initializes UI components for the promotion panel.
     * Sets up the text fields, buttons, and table with column definitions.
     */
    private void initializeComponents() {
        dateField = new DatePicker();
        dateField.setPromptText("Date (YYYY-MM-DD)");

        textArea = new TextArea();
        textArea.setPromptText("Promotion Text");
        textArea.setPrefRowCount(5);

        addButton = new Button("Add Promotion");
        updateButton = new Button("Update Promotion");
        deleteButton = new Button("Delete Promotion");
        refreshButton = new Button("Refresh");

        promotionData = FXCollections.observableArrayList();
        promotionTable = new TableView<>();

        // Create columns
        TableColumn<Promotion, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().promotionDateProperty());

        TableColumn<Promotion, String> textColumn = new TableColumn<>("Promotion Text");
        textColumn.setCellValueFactory(cellData -> cellData.getValue().promotionTextProperty());

        promotionTable.getColumns().addAll(dateColumn, textColumn);
        promotionTable.setItems(promotionData);
    }

    /**
     * Sets up layout structure of the promotion panel.
     * Configures the spacing, padding, and arrangement of the UI components.
     */
    private void setupLayout() {
        setPadding(new Insets(10));
        setSpacing(10);

        // Input panel
        GridPane inputPanel = new GridPane();
        inputPanel.setPadding(new Insets(10));
        inputPanel.setHgap(10);
        inputPanel.setVgap(10);

        inputPanel.add(new Label("Date (YYYY-MM-DD):"), 0, 0);
        inputPanel.add(dateField, 1, 0);

        inputPanel.add(new Label("Promotion Text:"), 0, 1);
        inputPanel.add(textArea, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton);

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(inputPanel, buttonBox);

        // Results panel
        ScrollPane scrollPane = new ScrollPane(promotionTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        VBox resultsBox = new VBox(10);
        resultsBox.getChildren().add(scrollPane);

        getChildren().addAll(inputBox, resultsBox);
    }

    /**
     * Sets up event handlers for UI interactions.
     * Connects button clicks and table selection to action methods.
     */
    private void setupEventHandlers() {
        addButton.setOnAction(e -> addPromotion());
        updateButton.setOnAction(e -> updatePromotion());
        deleteButton.setOnAction(e -> deletePromotion());
        refreshButton.setOnAction(e -> refreshPromotions());

        promotionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadPromotionData(newSelection);
            }
        });
    }

    /**
     * Sets the user for role-based access control
     * @param currentUser The current authenticated user
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        isEditable = !"Customer".equals(currentUser.getRole());
        updateButtonState();
    }

    /**
     * Updates button visibility based on the user role
     */
    private void updateButtonState() {
        addButton.setDisable(!isEditable);
        updateButton.setDisable(!isEditable);
        deleteButton.setDisable(!isEditable);
    }

    /**
     * Adds a promotion.
     * Validates the input and refreshes the table display after successful addition.
     */
    private void addPromotion() {
        Promotion promotion = createPromotionFromInput();
        if (promotion != null) {
            if (controller.addPromotion(promotion)) {
                showAlert("Success", "Promotion added successfully");
                refreshPromotions();
                clearInputFields();
            } else {
                showAlert("Error", "Failed to add promotion");
            }
        }
    }

    /**
     * Updates a promotion.
     * Uses selected item reference to preserve the ID during the update operation.
     */
    private void updatePromotion() {
        Promotion selected = promotionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a promotion to update");
            return;
        }

        Promotion promotion = createPromotionFromInput();
        if (promotion != null) {
            // Preserve the ID from the original selection
            promotion.setPromotionId(selected.getPromotionId());

            if (controller.updatePromotion(promotion)) {
                showAlert("Success", "Promotion updated successfully");
                refreshPromotions();
            } else {
                showAlert("Error", "Failed to update promotion");
            }
        }
    }

    /**
     * Deletes a promotion.
     * Prompts the user confirmation before proceeding with the deletion.
     */
    private void deletePromotion() {
        Promotion selected = promotionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a promotion to delete");
            return;
        }

        if (confirmAction("Are you sure you want to delete this promotion?")) {
            if (controller.deletePromotion(selected.getPromotionId())) { // Use actual ID
                showAlert("Success", "Promotion deleted successfully");
                refreshPromotions();
            } else {
                showAlert("Error", "Failed to delete promotion");
            }
        }
    }

    /**
     * Refreshes the promotion table.
     * This ensures that the auto-generated IDs are properly loaded after record creation.
     */
    private void refreshPromotions() {
        List<Promotion> promotions = controller.getAllPromotions();
        promotionData.clear();
        promotionData.addAll(promotions);
    }

    /**
     * Creates a Promotion object from user input fields and validates required fields and date values.
     * @return Promotion object If the validation passes, null otherwise
     */
    private Promotion createPromotionFromInput() {
        java.time.LocalDate localDate = dateField.getValue();
        String text = textArea.getText().trim();

        if (localDate == null || text.isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return null;
        }

        try {
            // Convert LocalDate to Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(localDate.toString());

            Promotion promotion = new Promotion();
            promotion.setPromotionDate(date);
            promotion.setPromotionText(text);

            return promotion;
        } catch (Exception e) {
            showAlert("Error", "Please enter a valid date in YYYY-MM-DD format");
            return null;
        }
    }

    /**
     * Loads data from the Promotion object into the input fields.
     * @param promotion The Promotion object to load data from
     */
    private void loadPromotionData(Promotion promotion) {
        // Convert Date to LocalDate for DatePicker
        java.util.Date date = promotion.getPromotionDate();
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.time.LocalDate localDate = java.time.LocalDate.parse(sdf.format(date));
                dateField.setValue(localDate);
            } catch (Exception e) {
                dateField.setValue(null);
            }
        } else {
            dateField.setValue(null);
        }
        textArea.setText(promotion.getPromotionText());
    }

    /**
     * Clears all of the input fields in the form.
     * Used after the successful addition or when resetting the form.
     */
    private void clearInputFields() {
        dateField.setValue(null);
        textArea.clear();
    }
}
