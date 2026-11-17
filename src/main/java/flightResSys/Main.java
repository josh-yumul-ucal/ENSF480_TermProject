package flightResSys;

import javafx.application.Application;

public class Main {
    public static void main(String[] args){
      Application.launch(FlightGUI.class, args);
    }
}
  //     // throws SQLException lets program show JDBC errors


  //     // String holds directory for database
  //     String url = "jdbc:sqlite:data/flights.db";
  //     String testDB = "data/flights.db";

  //     // Connection = java interface that represents an active db connection
  //     // DriverManager.getConnection(url) finds JDBC driver that can open the url (will create if no file there)


  //     try(Connection conn = DriverManager.getConnection(url)){


  //       // JDBC interface that lets you send SQL commands
  //       // stmt is connected to conn object, which is connected to our practice db
  //       Statement stmt = conn.createStatement();

  //       // .executeUpdate performs SQL statements that MODIFY data on the database the statement is connected to
  //       stmt.executeUpdate("""
  //         CREATE TABLE IF NOT EXISTS Flights (
  //           id INTEGER PRIMARY KEY,
  //           destination TEXT,
  //           passenger TEXT
  //         )
  //         """);

  //       stmt.executeUpdate("DELETE from Flights");

  //       stmt.executeUpdate("""
  //         INSERT INTO Flights(destination, passenger)
  //         VALUES ('New York City', 'Tony Stark')
  //       """);

  //       // ResultSet stores results from a .executeQuery
  //       // .executeQuery is for SQL commands that retrieves data
  //       ResultSet rs = stmt.executeQuery("SELECT * FROM Flights");
  //       while (rs.next()){
  //         System.out.println(
  //           rs.getInt("id") + " | " +
  //           rs.getString("destination") + " | " +
  //           rs.getString("passenger")
  //         );
  //       }

  //       rs.close();
  //       stmt.close();

  //   }
  //   // chunk that prints out stack trace error for debugging purposes
  //   catch (SQLException e){
  //     e.printStackTrace();
  //   }
  // }

