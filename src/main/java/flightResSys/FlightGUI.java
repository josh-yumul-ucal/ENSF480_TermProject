package flightResSys;

import javax.swing.*;
import java.awt.*;

public class FlightGUI extends JFrame{
  public FlightGUI(){
    setTitle("Flight Reservation System GUI test");
    setSize(800, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    setLayout(new FlowLayout());

    // Adds new JButton objects
    JButton searchButton = new JButton("Search Flights");
    JButton sqlTestButton = new JButton("Test SQL database");
    JLabel statusLabel = new JLabel("Welcome!");

    // e == ActionEvent == user action, which is passed as a parameter into a lambda expression to perform the button's functions
    searchButton.addActionListener(e -> {
      statusLabel.setText("The button worked :D");
    });

    sqlTestButton.addActionListener(e -> {
      FlightDatabase.testDB();
      statusLabel.setText("SQL Test executed");
    });

    add (searchButton);
    add (statusLabel);
    add (sqlTestButton);
  }
}
