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
import model.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/**
 * View component for user management.
 * Provides a user interface for adding, updating, and deleting the user accounts.
 * Supports role-based access control with different permissions for each type of user.
 */
public class UserManagementPanel extends BasePane {
    private MainController controller;

    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox; // Changed from TextField to ComboBox
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button clearButton;
    private Button refreshButton;
    private TableView<User> userTable;
    private ObservableList<User> userData;

    /**
     * Initializes the user management panel with the controller and UI components.
     * Sets up layout, event handlers, and initial data display.
     * @param controller The main application controller
     */
    public UserManagementPanel(MainController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshUsers();
    }

    /**
     * Initializes all of the UI components for the user management panel.
     * Sets up the text fields, password fields, combo boxes, and table with column definitions.
     */
    private void initializeComponents() {
        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Create role dropdown with predefined options
        ObservableList<String> roles = FXCollections.observableArrayList(
                "Customer",
                "Flight Agent",
                "System Administrator"
        );
        roleComboBox = new ComboBox<>(roles);
        roleComboBox.setPromptText("Select Role");
        roleComboBox.setValue("Customer"); // Default value

        firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        emailField = new TextField();
        emailField.setPromptText("Email");

        addButton = new Button("Add User");
        updateButton = new Button("Update User");
        deleteButton = new Button("Delete User");
        clearButton = new Button("Clear");
        refreshButton = new Button("Refresh");

        userData = FXCollections.observableArrayList();
        userTable = new TableView<>();

        // Create columns
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());

        TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        userTable.getColumns().addAll(usernameColumn, roleColumn, firstNameColumn,
                lastNameColumn, emailColumn);
        userTable.setItems(userData);
    }

    /**
     * Sets up layout structure for the user management panel.
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

        inputPanel.add(new Label("Username:"), 0, 0);
        inputPanel.add(usernameField, 1, 0);

        inputPanel.add(new Label("Password:"), 0, 1);
        inputPanel.add(passwordField, 1, 1);

        inputPanel.add(new Label("Role:"), 0, 2);
        inputPanel.add(roleComboBox, 1, 2); // Changed to ComboBox

        inputPanel.add(new Label("First Name:"), 0, 3);
        inputPanel.add(firstNameField, 1, 3);

        inputPanel.add(new Label("Last Name:"), 0, 4);
        inputPanel.add(lastNameField, 1, 4);

        inputPanel.add(new Label("Email:"), 0, 5);
        inputPanel.add(emailField, 1, 5);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton, refreshButton);

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(inputPanel, buttonBox);

        // Results panel
        ScrollPane scrollPane = new ScrollPane(userTable);
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
        addButton.setOnAction(e -> addUser());
        updateButton.setOnAction(e -> updateUser());
        deleteButton.setOnAction(e -> deleteUser());
        clearButton.setOnAction(e -> clearInputFields());
        refreshButton.setOnAction(e -> refreshUsers());

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadUserData(newSelection);
            }
        });
    }

    /**
     * Adds a new user account.
     * Validates the input and refreshes the table display after successful creation.
     */
    private void addUser() {
        User user = createUserFromInput();
        if (user != null) {
            if (controller.addUser(user)) {
                showAlert("Success", "User added successfully");
                refreshUsers();
                clearInputFields();
            } else {
                showAlert("Error", "Failed to add user");
            }
        }
    }

    /**
     * Updates an existing user account.
     * Uses selected item reference to preserve ID during the update operation.
     */
    private void updateUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a user to update");
            return;
        }

        User user = createUserFromInput();
        if (user != null) {
            // Preserve the ID from the original selection
            user.setUserId(selected.getUserId());

            if (controller.updateUser(user)) {
                showAlert("Success", "User updated successfully");
                refreshUsers();
            } else {
                showAlert("Error", "Failed to update user");
            }
        }
    }

    /**
     * Deletes a user account.
     * Prompts user confirmation before proceeding with the deletion.
     */
    private void deleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a user to delete");
            return;
        }

        if (confirmAction("Are you sure you want to delete this user?")) {
            if (controller.deleteUser(selected.getUserId())) { // Use actual ID
                showAlert("Success", "User deleted successfully");
                refreshUsers();
            } else {
                showAlert("Error", "Failed to delete user");
            }
        }
    }

    /**
     * Refreshes the user table.
     * This ensures that auto-generated IDs are properly loaded after the record creation.
     */
    private void refreshUsers() {
        List<User> users = controller.getAllUsers();
        userData.clear();
        userData.addAll(users);
    }

    /**
     * Creates a User object from the user input fields.
     * Validates the required fields and parses role information.
     * @return User object if validation passes, null otherwise
     */
    private User createUserFromInput() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleComboBox.getValue(); // Get value from ComboBox
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || role == null ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return null;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role); // Set from ComboBox value
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        return user;
    }

    /**
     * Loads data from User object into the input fields.
     * Used when selecting an item to update its information.
     * @param user The User object used to load data from
     */
    private void loadUserData(User user) {
        usernameField.setText(user.getUsername());
        passwordField.clear(); // Don't show password
        roleComboBox.setValue(user.getRole()); // Set value in ComboBox
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
    }

    /**
     * Clears all of the input fields in the form.
     * Used after the successful creation or when resetting the form.
     */
    private void clearInputFields() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue("Customer"); // Reset to default
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
    }
}
