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
import javafx.stage.Stage;

/**
 * View component for login interface.
 * Provides the authentication functionality for users to access the application.
 * Manages user credentials and redirects to the appropriate main views based on role.
 */
public class LoginPanel extends BasePane {
    private MainController controller;
    private Stage primaryStage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button cancelButton;

    /**
     * Initializes the login panel with the controller and UI components.
     * Sets up layout, event handlers, and initial display state.
     * @param controller The main application controller
     * @param primaryStage The primary JavaFX stage
     */
    public LoginPanel(MainController controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    /**
     * Initializes UI components for the login panel.
     * Sets up the text fields, password fields, and buttons for authentication.
     */
    private void initializeComponents() {
        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        loginButton = new Button("Login");
        cancelButton = new Button("Cancel");
    }

    /**
     * Sets up layout structure for the login panel.
     * Configures the spacing, padding, and arrangement of UI components.
     */
    private void setupLayout() {
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(javafx.geometry.Pos.CENTER);

        // Create grid pane for the form elements
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Add the title
        Label titleLabel = new Label("ENSF480 Group 1 Flight Reservation System - Login");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        GridPane.setColumnSpan(titleLabel, 4);
        grid.add(titleLabel, 0, 0);

        // Username row
        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        GridPane.setColumnSpan(usernameField, 2);

        // Add the password row
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        GridPane.setColumnSpan(passwordField, 2);

        // Add the button row
        grid.add(loginButton, 1,3);
        grid.add(cancelButton, 3, 3);

        getChildren().add(grid);
    }

    /**
     * Sets up the event handlers for UI interactions.
     * Connects the button clicks to authentication and application exit methods.
     */
    private void setupEventHandlers() {
        loginButton.setOnAction(e -> performLogin());
        cancelButton.setOnAction(e -> System.exit(0));
    }

    /**
     * Processes the login attempt by validating the user credentials.
     * Authenticates against database and displays the appropriate feedback messages.
     */
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both username and password");
            return;
        }

        // Authenticate user
        User user = controller.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            showAlert("Success", "Login successful! Welcome " + user.getFirstName());

            // Switch to main application view
            controller.showMainApplication(user, primaryStage);
        } else {
            showAlert("Login Error", "Invalid username or password");
        }
    }

    /**
     * Displays login interface to the user.
     * Sets up the stage and shows login panel.
     */
    public void show() {
        primaryStage.setTitle("ENSF480 Group 1 Flight Reservation System - Login");
        primaryStage.setScene(new javafx.scene.Scene(this, 440, 250));
        primaryStage.show();
    }
}
