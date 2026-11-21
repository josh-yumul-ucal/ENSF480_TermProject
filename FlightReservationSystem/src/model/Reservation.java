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
 * Model class representing a flight reservation.
 * Tracks bookings, flight details, and reservation status.
 */
public class Reservation {
    private final IntegerProperty reservationId;
    private final IntegerProperty customerId;
    private final IntegerProperty flightId;
    private final ObjectProperty<Date> bookingDate;
    private final IntegerProperty numberOfSeats;
    private final DoubleProperty totalAmount;
    private final StringProperty status;

    /**
     * Default constructor for creating a new reservation object.
     */
    public Reservation() {
        this.reservationId = new SimpleIntegerProperty();
        this.customerId = new SimpleIntegerProperty();
        this.flightId = new SimpleIntegerProperty();
        this.bookingDate = new SimpleObjectProperty<>();
        this.numberOfSeats = new SimpleIntegerProperty();
        this.totalAmount = new SimpleDoubleProperty();
        this.status = new SimpleStringProperty();
    }

    /**
     * Constructor for creating a reservation object with predefined values.
     * @param reservationId The unique identifier for the reservation
     * @param customerId The unique identifier for the customer
     * @param flightId The unique identifier for the flight
     * @param bookingDate The date that the reservation was made
     * @param numberOfSeats The number of seats that were reserved
     * @param totalAmount The total cost of reservation
     * @param status The status of the reservation (confirmed, cancelled, etc.)
     */
    public Reservation(int reservationId, int customerId, int flightId, Date bookingDate,
                       int numberOfSeats, double totalAmount, String status) {
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.flightId = new SimpleIntegerProperty(flightId);
        this.bookingDate = new SimpleObjectProperty<>(bookingDate);
        this.numberOfSeats = new SimpleIntegerProperty(numberOfSeats);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.status = new SimpleStringProperty(status);
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the reservation.
     * @return The reservation ID
     */
    public int getReservationId() { return reservationId.get(); }

    /**
     * Sets the unique identifier for the reservation.
     * @param reservationId The new reservation ID
     */
    public void setReservationId(int reservationId) { this.reservationId.set(reservationId); }

    /**
     * Gets JavaFX property for reservation ID.
     * @return The reservation ID property
     */
    public IntegerProperty reservationIdProperty() { return reservationId; }

    /**
     * Gets the unique identifier for the customer who made the reservation.
     * @return The customer ID
     */
    public int getCustomerId() { return customerId.get(); }

    /**
     * Sets the unique identifier for the customer who made the reservation.
     * @param customerId The new customer ID
     */
    public void setCustomerId(int customerId) { this.customerId.set(customerId); }

    /**
     * Gets JavaFX property for customer ID.
     * @return The customer ID property
     */
    public IntegerProperty customerIdProperty() { return customerId; }

    /**
     * Gets the unique identifier of the flight for the reservation.
     * @return The flight ID
     */
    public int getFlightId() { return flightId.get(); }

    /**
     * Sets the unique identifier for the flight of this reservation.
     * @param flightId The new flight ID
     */
    public void setFlightId(int flightId) { this.flightId.set(flightId); }

    /**
     * Gets JavaFX property for flight ID.
     * @return The flight ID property
     */
    public IntegerProperty flightIdProperty() { return flightId; }

    /**
     * Gets the creation date for the reservation.
     * @return The booking date
     */
    public Date getBookingDate() { return bookingDate.get(); }

    /**
     * Sets the creation date for the reservation.
     * @param bookingDate The new booking date
     */
    public void setBookingDate(Date bookingDate) { this.bookingDate.set(bookingDate); }

    /**
     * Gets JavaFX property for booking date.
     * @return The booking date property
     */
    public ObjectProperty<Date> bookingDateProperty() { return bookingDate; }

    /**
     * Gets the number of seats reserved in the reservation.
     * @return The number of seats
     */
    public int getNumberOfSeats() { return numberOfSeats.get(); }

    /**
     * Sets the number of seats reserved in the reservation.
     * @param numberOfSeats The new number of seats
     */
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats.set(numberOfSeats); }

    /**
     * Gets JavaFX property for number of seats.
     * @return The number of seats property
     */
    public IntegerProperty numberOfSeatsProperty() { return numberOfSeats; }

    /**
     * Gets the total cost for the reservation.
     * @return The total amount
     */
    public double getTotalAmount() { return totalAmount.get(); }

    /**
     * Sets the total cost for this reservation.
     * @param totalAmount The new total amount
     */
    public void setTotalAmount(double totalAmount) { this.totalAmount.set(totalAmount); }

    /**
     * Gets JavaFX property for total amount.
     * @return The total amount property
     */
    public DoubleProperty totalAmountProperty() { return totalAmount; }

    /**
     * Gets the status of the reservation.
     * @return The status (confirmed, cancelled, etc.)
     */
    public String getStatus() { return status.get(); }

    /**
     * Sets the status of the reservation.
     * @param status The new status
     */
    public void setStatus(String status) { this.status.set(status); }

    /**
     * Gets JavaFX property for status.
     * @return The status property
     */
    public StringProperty statusProperty() { return status; }
}
