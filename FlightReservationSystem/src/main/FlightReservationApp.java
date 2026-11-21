/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package main;

import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This is the main entry point for the Flight Reservation System application.
 * Initializes and launches the JavaFX application.
 */
public class FlightReservationApp extends Application {

    /**
     * Starts the JavaFX application.
     * @param primaryStage The primary stage of the JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController();
        controller.start(primaryStage);
    }

    /**
     * Launches the Flight Reservation System in JavaFX
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
