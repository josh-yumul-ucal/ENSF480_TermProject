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
import model.Payment;
import model.Reservation;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * View component for payment management.
 * Provides a user interface for viewing, adding, and managing the payment records.
 * Displays the transaction details including reservation associations and payment methods.
 */
public class PaymentPanel extends VBox {
    private MainController controller;

    private ComboBox<Reservation> reservationComboBox;
    private TextField paymentDateField; // Using text field for date input
    private TextField amountField;
    private TextField methodField;
    private TextField transactionIdField;
    private Button addButton;
    private Button refreshButton;
    private TableView<Payment> paymentTable;
    private ObservableList<Payment> paymentData;

    /**
     * Initializes the payment panel with the controller and UI components.
     * Sets up layout, event handlers, and initial data display.
     * @param controller The main application controller
     */
    public PaymentPanel(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshPayments();
    }

    /**
     * Initializes UI components for the payment panel.
     * Sets up the text fields, buttons, and table with column definitions.
     */
    private void initializeComponents() {
        // Create reservation dropdown
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        reservationComboBox = new ComboBox<>(reservationList);
        reservationComboBox.setPromptText("Select Reservation");

        // Set converter for reservation combo box
        reservationComboBox.setConverter(new javafx.util.StringConverter<Reservation>() {
            @Override
            public String toString(Reservation reservation) {
                if (reservation == null) return "";
                return "Customer_" + reservation.getCustomerId() + " (" + reservation.getReservationId() + ")";
            }

            @Override
            public Reservation fromString(String string) {
                return null; // Not needed for this use case
            }
        });

        paymentDateField = new TextField();
        paymentDateField.setPromptText("Payment Date (YYYY-MM-DD HH:MM)");

        amountField = new TextField();
        amountField.setPromptText("Amount");

        methodField = new TextField();
        methodField.setPromptText("Payment Method");

        transactionIdField = new TextField();
        transactionIdField.setPromptText("Transaction ID");

        addButton = new Button("Add Payment");
        refreshButton = new Button("Refresh");

        paymentData = FXCollections.observableArrayList();
        paymentTable = new TableView<>();

        // Create columns
        TableColumn<Payment, Integer> reservationIdColumn = new TableColumn<>("Reservation ID");
        reservationIdColumn.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty().asObject());

        TableColumn<Payment, Date> dateColumn = new TableColumn<>("Payment Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().paymentDateProperty());

        TableColumn<Payment, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

        TableColumn<Payment, String> methodColumn = new TableColumn<>("Method");
        methodColumn.setCellValueFactory(cellData -> cellData.getValue().paymentMethodProperty());

        TableColumn<Payment, String> transactionIdColumn = new TableColumn<>("Transaction ID");
        transactionIdColumn.setCellValueFactory(cellData -> cellData.getValue().transactionIdProperty());

        paymentTable.getColumns().addAll(reservationIdColumn, dateColumn, amountColumn,
                methodColumn, transactionIdColumn);
        paymentTable.setItems(paymentData);
    }

    /**
     * Sets up layout structure for the payment panel.
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

        inputPanel.add(new Label("Reservation:"), 0, 0);
        inputPanel.add(reservationComboBox, 1, 0);

        inputPanel.add(new Label("Payment Date (YYYY-MM-DD HH:MM):"), 0, 1);
        inputPanel.add(paymentDateField, 1, 1);

        inputPanel.add(new Label("Amount:"), 0, 2);
        inputPanel.add(amountField, 1, 2);

        inputPanel.add(new Label("Payment Method:"), 0, 3);
        inputPanel.add(methodField, 1, 3);

        inputPanel.add(new Label("Transaction ID:"), 0, 4);
        inputPanel.add(transactionIdField, 1, 4);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, refreshButton);

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(inputPanel, buttonBox);

        // Results panel
        ScrollPane scrollPane = new ScrollPane(paymentTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        VBox resultsBox = new VBox(10);
        resultsBox.getChildren().add(scrollPane);

        getChildren().addAll(inputBox, resultsBox);
    }

    /**
     * Sets up the event handlers for UI interactions.
     * Connects the button clicks to action methods.
     */
    private void setupEventHandlers() {
        addButton.setOnAction(e -> addPayment());
        refreshButton.setOnAction(e -> refreshPayments());
    }

    /**
     * Adds a new payment record.
     * Validates the input and refreshes the table display after successful creation.
     */
    private void addPayment() {
        Payment payment = createPaymentFromInput();
        if (payment != null) {
            if (controller.addPayment(payment)) {
                showAlert("Success", "Payment added successfully");
                refreshPayments();
                clearInputFields();
            } else {
                showAlert("Error", "Failed to add payment");
            }
        }
    }

    /**
     * Refreshes the payment table.
     * This ensures that the auto-generated IDs are properly loaded after record has been created.
     */
    private void refreshPayments() {
        List<Payment> payments = controller.getAllPayments();
        paymentData.clear();
        paymentData.addAll(payments);

        // Also populate reservation dropdown
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        for (Reservation reservation : controller.getAllReservations()) {
            reservationList.add(reservation);
        }
        reservationComboBox.setItems(reservationList);
        reservationComboBox.setValue(null);
    }

    /**
     * Creates a Payment object from the user input fields.
     * Validates the required fields and parses date/time values.
     * @return Payment object if validation passes, null otherwise
     */
    private Payment createPaymentFromInput() {
        Reservation selectedReservation = reservationComboBox.getValue();
        String paymentDateStr = paymentDateField.getText().trim();
        String amountStr = amountField.getText().trim();
        String method = methodField.getText().trim();
        String transactionId = transactionIdField.getText().trim();

        if (selectedReservation == null || paymentDateStr.isEmpty() ||
                amountStr.isEmpty() || method.isEmpty() || transactionId.isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return null;
        }

        try {
            int reservationId = selectedReservation.getReservationId();
            double amount = Double.parseDouble(amountStr);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date paymentDate = sdf.parse(paymentDateStr);

            Payment payment = new Payment();
            payment.setReservationId(reservationId);
            payment.setPaymentDate(paymentDate);
            payment.setAmount(amount);
            payment.setPaymentMethod(method);
            payment.setTransactionId(transactionId);

            return payment;
        } catch (Exception e) {
            showAlert("Error", "Please enter valid numbers and a valid date in YYYY-MM-DD HH:MM format");
            return null;
        }
    }

    /**
     * Clears all of the input fields in the form.
     * Used after the successful creation or when resetting the form.
     */
    private void clearInputFields() {
        reservationComboBox.setValue(null);
        paymentDateField.clear();
        amountField.clear();
        methodField.clear();
        transactionIdField.clear();
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
}
