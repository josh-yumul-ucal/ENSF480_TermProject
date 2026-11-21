/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database manager responsible for establishing database connections and initializing
 * the database schema.
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:flight_reservation.db";
    private static DatabaseManager instance;

    /**
     * Initializes database connection.
     * @throws ClassNotFoundException If SQLite JDBC driver is not found
     */
    private DatabaseManager() {
        /** Throw an exception if the sqlite JDBC driver does not exist */
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the instance of the DatabaseManager.
     * Ensures only one database connection manager exists within the application.
     * @return The DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Establishes a connection to the database.
     * @return A Connection object for the database operations
     * @throws SQLException If the connection can not be established
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Initializes the database schema by creating all the tables that are required.
     * Sets up the data model including users, flights, reservations, and payments.
     * Inserts initial admin user if not already present.
     */
    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create Users table
            String createUserTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT NOT NULL
                )
                """;

            // Create Aircraft table
            String createAircraftTable = """
                CREATE TABLE IF NOT EXISTS aircraft (
                    aircraft_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    aircraft_name TEXT NOT NULL,
                    model TEXT NOT NULL,
                    capacity INTEGER NOT NULL
                )
                """;

            // Create Flights table
            String createFlightsTable = """
                CREATE TABLE IF NOT EXISTS flights (
                    flight_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    flight_number TEXT UNIQUE NOT NULL,
                    origin TEXT NOT NULL,
                    destination TEXT NOT NULL,
                    departure_time TIMESTAMP NOT NULL,
                    arrival_time TIMESTAMP NOT NULL,
                    airline TEXT NOT NULL,
                    total_seats INTEGER NOT NULL,
                    available_seats INTEGER NOT NULL,
                    price REAL NOT NULL
                )
                """;

            // Create Customers table
            String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT,
                    date_of_birth DATE
                )
                """;

            // Create Reservations table
            String createReservationsTable = """
                CREATE TABLE IF NOT EXISTS reservations (
                    reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER NOT NULL,
                    flight_id INTEGER NOT NULL,
                    booking_date TIMESTAMP NOT NULL,
                    number_of_seats INTEGER NOT NULL,
                    total_amount REAL NOT NULL,
                    status TEXT NOT NULL,
                    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
                    FOREIGN KEY (flight_id) REFERENCES flights (flight_id)
                )
                """;

            // Create Payments table
            String createPaymentsTable = """
                CREATE TABLE IF NOT EXISTS payments (
                    payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    reservation_id INTEGER NOT NULL,
                    payment_date TIMESTAMP NOT NULL,
                    amount REAL NOT NULL,
                    payment_method TEXT NOT NULL,
                    transaction_id TEXT UNIQUE NOT NULL,
                    FOREIGN KEY (reservation_id) REFERENCES reservations (reservation_id)
                )
                """;

            // Execute all table creation statements
            stmt.execute(createUserTable);
            stmt.execute(createAircraftTable);
            stmt.execute(createFlightsTable);
            stmt.execute(createCustomersTable);
            stmt.execute(createReservationsTable);
            stmt.execute(createPaymentsTable);

            // Insert initial admin user if not exists
            String checkAdminUser = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
            try (var rs = stmt.executeQuery(checkAdminUser)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertAdminUser = "INSERT INTO users VALUES (1,'admin','admin123','System Administrator','Joe','Dirt','joe@soil.com')";
                    stmt.execute(insertAdminUser);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
