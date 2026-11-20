package flightResSys;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FlightGUI extends Application {
  @Override
  public void start(Stage stage) {
    stage.setTitle("Flight Reservation System GUI test");

    Button searchButton = new Button("Search Flights");
    Button sqlTestButton = new Button("Test SQL database");
    Label statusLabel = new Label("Welcome!");

    // e == ActionEvent == user action, which is passed as a parameter into a lambda expression to perform the button's functions
    searchButton.setOnAction(e -> {
      statusLabel.setText("The button worked :D");
    });

    sqlTestButton.setOnAction(e -> {
      FlightDatabase.testDB();
      statusLabel.setText("SQL Test executed");
    });

    FlowPane root = new FlowPane(5, 5, searchButton, statusLabel, sqlTestButton);
    stage.setScene(new Scene(root, 800, 800));
    stage.show();
    stage.centerOnScreen();
  }
}
