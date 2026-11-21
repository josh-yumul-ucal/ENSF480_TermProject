/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package model;

import javafx.beans.property.*;
import java.util.Date;

/**
 * Model class representing a flight.
 * Contains scheduling, routing, and pricing details.
 */
public class Flight {
    private final IntegerProperty flightId;
    private final StringProperty flightNumber;
    private final StringProperty origin;
    private final StringProperty destination;
    private final ObjectProperty<Date> departureTime;
    private final ObjectProperty<Date> arrivalTime;
    private final StringProperty airline;
    private final IntegerProperty totalSeats;
    private final IntegerProperty availableSeats;
    private final DoubleProperty price;
    private final ObjectProperty<Aircraft> aircraft;

    // Constructors

    /**
     * Default constructor for creating a new flight object.
     */
    public Flight() {
        this.flightId = new SimpleIntegerProperty();
        this.flightNumber = new SimpleStringProperty();
        this.origin = new SimpleStringProperty();
        this.destination = new SimpleStringProperty();
        this.departureTime = new SimpleObjectProperty<>();
        this.arrivalTime = new SimpleObjectProperty<>();
        this.airline = new SimpleStringProperty();
        this.totalSeats = new SimpleIntegerProperty();
        this.availableSeats = new SimpleIntegerProperty();
        this.price = new SimpleDoubleProperty();
        this.aircraft = new SimpleObjectProperty<>();
    }

    /**
     * Constructor for creating a new flight object with predefined values.
     * @param flightId The unique identifier of the flight
     * @param flightNumber The flight number
     * @param origin The departure airport code or city name
     * @param destination The arrival airport code or city name
     * @param departureTime The departure date and time
     * @param arrivalTime The arrival date and time
     * @param airline The airline operating the flight
     * @param totalSeats The total number of seats available
     * @param availableSeats The number of seats available for booking
     * @param price The base fare for a seat on this flight
     */
    public Flight(int flightId, String flightNumber, String origin, String destination,
                  Date departureTime, Date arrivalTime, String airline, int totalSeats,
                  int availableSeats, double price) {
        this.flightId = new SimpleIntegerProperty(flightId);
        this.flightNumber = new SimpleStringProperty(flightNumber);
        this.origin = new SimpleStringProperty(origin);
        this.destination = new SimpleStringProperty(destination);
        this.departureTime = new SimpleObjectProperty<>(departureTime);
        this.arrivalTime = new SimpleObjectProperty<>(arrivalTime);
        this.airline = new SimpleStringProperty(airline);
        this.totalSeats = new SimpleIntegerProperty(totalSeats);
        this.availableSeats = new SimpleIntegerProperty(availableSeats);
        this.price = new SimpleDoubleProperty(price);
        this.aircraft = new SimpleObjectProperty<>();
    }

    /**
     * Gets the unique identifier for the flight.
     * @return The flight ID
     */
    public int getFlightId() { return flightId.get(); }

    /**
     * Sets the unique identifier for the flight.
     * @param flightId The new flight ID
     */
    public void setFlightId(int flightId) { this.flightId.set(flightId); }

    /**
     * Gets JavaFX property for the flight ID.
     * @return The flight ID property
     */
    public IntegerProperty flightIdProperty() { return flightId; }

    /**
     * Gets the flight number.
     * @return The flight number
     */
    public String getFlightNumber() { return flightNumber.get(); }

    /**
     * Sets the flight number.
     * @param flightNumber The new flight number
     */
    public void setFlightNumber(String flightNumber) { this.flightNumber.set(flightNumber); }

    /**
     * Gets JavaFX property for the flight number.
     * @return The flight number property
     */
    public StringProperty flightNumberProperty() { return flightNumber; }

    /**
     * Gets the departure airport code or the city name.
     * @return The origin location
     */
    public String getOrigin() { return origin.get(); }

    /**
     * Sets the departure airport code or the city name.
     * @param origin The new origin location
     */
    public void setOrigin(String origin) { this.origin.set(origin); }

    /**
     * Gets JavaFX property for origin.
     * @return The origin property
     */
    public StringProperty originProperty() { return origin; }

    /**
     * Gets the arrival airport code or the city name.
     * @return The destination location
     */
    public String getDestination() { return destination.get(); }

    /**
     * Sets the arrival airport code or the city name.
     * @param destination The new destination location
     */
    public void setDestination(String destination) { this.destination.set(destination); }

    /**
     * Gets JavaFX property for destination.
     * @return The destination property
     */
    public StringProperty destinationProperty() { return destination; }

    /**
     * Gets the departure date and time.
     * @return The departure time
     */
    public Date getDepartureTime() { return departureTime.get(); }

    /**
     * Sets the departure date and time.
     * @param departureTime The new departure time
     */
    public void setDepartureTime(Date departureTime) { this.departureTime.set(departureTime); }

    /**
     * Gets JavaFX property for departure time.
     * @return The departure time property
     */
    public ObjectProperty<Date> departureTimeProperty() { return departureTime; }

    /**
     * Gets the arrival date and time.
     * @return The arrival time
     */
    public Date getArrivalTime() { return arrivalTime.get(); }

    /**
     * Sets the arrival date and time.
     * @param arrivalTime The new arrival time
     */
    public void setArrivalTime(Date arrivalTime) { this.arrivalTime.set(arrivalTime); }

    /**
     * Gets JavaFX property for arrival time.
     * @return The arrival time property
     */
    public ObjectProperty<Date> arrivalTimeProperty() { return arrivalTime; }

    /**
     * Gets the airline name who is operating the flight.
     * @return The airline name
     */
    public String getAirline() { return airline.get(); }

    /**
     * Sets the airline that is operating the flight.
     * @param airline The new airline name
     */
    public void setAirline(String airline) { this.airline.set(airline); }

    /**
     * Gets JavaFX property for airline.
     * @return The airline property
     */
    public StringProperty airlineProperty() { return airline; }

    /**
     * Gets the total number of seats available.
     * @return The total seat count
     */
    public int getTotalSeats() { return totalSeats.get(); }

    /**
     * Sets the total number of seats available.
     * @param totalSeats The new total seat count
     */
    public void setTotalSeats(int totalSeats) { this.totalSeats.set(totalSeats); }

    /**
     * Gets JavaFX property for total seats.
     * @return The total seats property
     */
    public IntegerProperty totalSeatsProperty() { return totalSeats; }

    /**
     * Gets the number of seats available for booking.
     * @return The available seat count
     */
    public int getAvailableSeats() { return availableSeats.get(); }

    /**
     * Sets the number of seats available for booking.
     * @param availableSeats The new available seat count
     */
    public void setAvailableSeats(int availableSeats) { this.availableSeats.set(availableSeats); }

    /**
     * Gets JavaFX property for available seats.
     * @return The available seats property
     */
    public IntegerProperty availableSeatsProperty() { return availableSeats; }

    /**
     * Gets the base fare for a seat on this flight.
     * @return The price per seat
     */
    public double getPrice() { return price.get(); }

    /**
     * Sets the base fare for a seat on this flight.
     * @param price The new price per seat
     */
    public void setPrice(double price) { this.price.set(price); }

    /**
     * Gets JavaFX property for price.
     * @return The price property
     */
    public DoubleProperty priceProperty() { return price; }

    /**
     * Gets the aircraft assigned to this flight.
     * @return The aircraft object
     */
    public Aircraft getAircraft() { return aircraft.get(); }

    /**
     * Sets the aircraft assigned to this flight.
     * @param aircraft The new aircraft object
     */
    public void setAircraft(Aircraft aircraft) { this.aircraft.set(aircraft); }

    /**
     * Gets JavaFX property for aircraft.
     * @return The aircraft property
     */
    public ObjectProperty<Aircraft> aircraftProperty() { return aircraft; }
}
