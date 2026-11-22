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
import model.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Main tabbed application view that coordinates functional panels and manages navigation
 * between login and main views.
 */
public class MainView extends BorderPane {
    private TabPane tabPane;
    private FlightSearchPanel flightSearchPanel;
    private ReservationManagementPanel reservationManagementPanel;
    private FlightManagementPanel flightManagementPanel;
    private PaymentPanel paymentPanel;
    private AircraftManagementPanel aircraftManagementPanel;
    private UserManagementPanel userManagementPanel;

    private MainController controller;
    private Stage primaryStage;

    /**
     * Initializes the main application view with controller and user data.
     * @param controller The main application controller
     * @param primaryStage The primary JavaFX stage
     */
    public MainView(MainController controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
        initializeComponents();
        setupLayout();
        setPadding(new Insets(10));
    }

    /**
     * Initializes all main application view UI components.
     * Sets up tab panels with role-based access to specific modules.
     */
    private void initializeComponents() {
        flightSearchPanel = new FlightSearchPanel(controller);
        reservationManagementPanel = new ReservationManagementPanel(controller);
        flightManagementPanel = new FlightManagementPanel(controller);
        paymentPanel = new PaymentPanel(controller);
        aircraftManagementPanel = new AircraftManagementPanel(controller);
        userManagementPanel = new UserManagementPanel(controller);

        tabPane = new TabPane();

        // Add tabs common to all user roles
        tabPane.getTabs().add(new Tab("Flight Search", flightSearchPanel));
        tabPane.getTabs().add(new Tab("Reservation Management", reservationManagementPanel));

        if (controller.getCurrentUser() != null) {
            // Add role-specific tabs
            if (controller.getCurrentUser().getRole().equals("System Administrator")) {
                tabPane.getTabs().add(new Tab("Flight Management", flightManagementPanel));
                tabPane.getTabs().add(new Tab("Aircraft Management", aircraftManagementPanel));
                tabPane.getTabs().add(new Tab("User Management", userManagementPanel));
            } else if (controller.getCurrentUser().getRole().equals("Flight Agent")) {
                tabPane.getTabs().add(new Tab("Flight Management", flightManagementPanel));
                tabPane.getTabs().add(new Tab("Aircraft Management", aircraftManagementPanel));
            }
        }

        // Note: Customer role should not have access to user management
        tabPane.getTabs().add(new Tab("Payment", paymentPanel));
    }

    /**
     * Sets up the layout structure for the main application view including
     * logout button functionality
     */
    private void setupLayout() {
        // Create logout button
        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> {
            controller.showLogin(primaryStage);
        });

        // Create top pane with a logout button
        HBox topPane = new HBox(10);
        topPane.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        topPane.setPadding(new Insets(5));
        topPane.getChildren().add(logoutButton);

        setTop(topPane);
        setCenter(tabPane);
        setPadding(new Insets(10));
    }

    /**
     * Displays the main application view.
     * Sets the stage title and shows the application window.
     */
    public void showView() {
        if (controller.getCurrentUser() != null) {
            primaryStage.setTitle("Flight Reservation System - " + controller.getCurrentUser().getRole() + " (" + controller.getCurrentUser().getUsername() + ")");
        }
        primaryStage.setScene(new javafx.scene.Scene(this, 1200, 700));
        primaryStage.show();
    }
}
