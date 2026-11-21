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
 * Model class representing a payment transaction.
 * Records the financial information for a reservation payments which includes
 * method and transaction identifiers.
 */
public class Payment {
    private final IntegerProperty paymentId;
    private final IntegerProperty reservationId;
    private final ObjectProperty<Date> paymentDate;
    private final DoubleProperty amount;
    private final StringProperty paymentMethod;
    private final StringProperty transactionId;

    /**
     * Default constructor for creating a new payment object.
     */
    public Payment() {
        this.paymentId = new SimpleIntegerProperty();
        this.reservationId = new SimpleIntegerProperty();
        this.paymentDate = new SimpleObjectProperty<>();
        this.amount = new SimpleDoubleProperty();
        this.paymentMethod = new SimpleStringProperty();
        this.transactionId = new SimpleStringProperty();
    }

    /**
     * Constructor for creating a payment object with predefined values.
     * @param paymentId The unique identifier for the payment
     * @param reservationId The unique identifier for the reservation
     * @param paymentDate The payment processing date
     * @param amount The amount of the payment
     * @param paymentMethod The payment method used (credit card, cash, etc.)
     * @param transactionId The unique transaction identifier
     */
    public Payment(int paymentId, int reservationId, Date paymentDate, double amount,
                   String paymentMethod, String transactionId) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
        this.amount = new SimpleDoubleProperty(amount);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.transactionId = new SimpleStringProperty(transactionId);
    }

    /**
     * Gets the unique identifier for the payment.
     * @return The payment ID
     */
    public int getPaymentId() { return paymentId.get(); }

    /**
     * Sets the unique identifier for the payment.
     * @param paymentId The new payment ID
     */
    public void setPaymentId(int paymentId) { this.paymentId.set(paymentId); }

    /**
     * Gets JavaFX property for payment ID.
     * @return The payment ID property
     */
    public IntegerProperty paymentIdProperty() { return paymentId; }

    /**
     * Gets the unique identifier of the reservation that is associated with this payment.
     * @return The reservation ID
     */
    public int getReservationId() { return reservationId.get(); }

    /**
     * Sets the unique identifier of the reservation that is associated with this payment.
     * @param reservationId The new reservation ID
     */
    public void setReservationId(int reservationId) { this.reservationId.set(reservationId); }

    /**
     * Gets JavaFX property for reservation ID.
     * @return The reservation ID property
     */
    public IntegerProperty reservationIdProperty() { return reservationId; }

    /**
     * Gets the payment processing date.
     * @return The payment date
     */
    public Date getPaymentDate() { return paymentDate.get(); }

    /**
     * Sets the date that the payment was processed.
     * @param paymentDate The new payment date
     */
    public void setPaymentDate(Date paymentDate) { this.paymentDate.set(paymentDate); }

    /**
     * Gets JavaFX property for payment date.
     * @return The payment date property
     */
    public ObjectProperty<Date> paymentDateProperty() { return paymentDate; }

    /**
     * Gets the amount paid.
     * @return The payment amount
     */
    public double getAmount() { return amount.get(); }

    /**
     * Sets the amount paid.
     * @param amount The new payment amount
     */
    public void setAmount(double amount) { this.amount.set(amount); }

    /**
     * Gets JavaFX property for amount.
     * @return The amount property
     */
    public DoubleProperty amountProperty() { return amount; }

    /**
     * Gets the payment method (credit card, cash, etc.).
     * @return The payment method
     */
    public String getPaymentMethod() { return paymentMethod.get(); }

    /**
     * Sets the payment method (credit card, cash, etc.).
     * @param paymentMethod The new payment method
     */
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod.set(paymentMethod); }

    /**
     * Gets JavaFX property for payment method.
     * @return The payment method property
     */
    public StringProperty paymentMethodProperty() { return paymentMethod; }

    /**
     * Gets the unique transaction identifier.
     * @return The transaction ID
     */
    public String getTransactionId() { return transactionId.get(); }

    /**
     * Sets the unique transaction identifier.
     * @param transactionId The new transaction ID
     */
    public void setTransactionId(String transactionId) { this.transactionId.set(transactionId); }

    /**
     * Gets JavaFX property for the transaction ID.
     * @return The transaction ID property
     */
    public StringProperty transactionIdProperty() { return transactionId; }

    /**
     * Returns a string representation of the payment object to be used for debugging.
     * @return Formatted string with payment information
     */
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", reservationId=" + reservationId +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
