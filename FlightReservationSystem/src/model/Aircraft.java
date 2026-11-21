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
 * Model class that represents an aircraft in the airline fleet.
 * Contains aircraft specifications and capacity.
 */
public class Aircraft {
    private final IntegerProperty aircraftId;
    private final StringProperty aircraftName;
    private final StringProperty model;
    private final IntegerProperty capacity;

    /**
     * Default constructor for creating a new aircraft object.
     */
    public Aircraft() {
        this.aircraftId = new SimpleIntegerProperty();
        this.aircraftName = new SimpleStringProperty();
        this.model = new SimpleStringProperty();
        this.capacity = new SimpleIntegerProperty();
    }

    /**
     * Constructor for creating a new aircraft object with predefined values.
     * @param aircraftId The unique identifier of the aircraft
     * @param aircraftName The aircraft name
     * @param model The aircraft model
     * @param capacity The passenger capacity for the aircraft
     */
    public Aircraft(int aircraftId, String aircraftName, String model, int capacity) {
        this.aircraftId = new SimpleIntegerProperty(aircraftId);
        this.aircraftName = new SimpleStringProperty(aircraftName);
        this.model = new SimpleStringProperty(model);
        this.capacity = new SimpleIntegerProperty(capacity);
    }

    /**
     * Gets the unique identifier for the aircraft.
     * @return The aircraft ID
     */
    public int getAircraftId() { return aircraftId.get(); }

    /**
     * Sets the unique identifier for the aircraft.
     * @param aircraftId The new aircraft ID
     */
    public void setAircraftId(int aircraftId) { this.aircraftId.set(aircraftId); }

    /**
     * Gets the JavaFX property for the aircraft ID.
     * @return The aircraft ID property
     */
    public IntegerProperty aircraftIdProperty() { return aircraftId; }

    /**
     * Gets the name of the aircraft.
     * @return The aircraft name
     */
    public String getAircraftName() { return aircraftName.get(); }

    /**
     * Sets the name of the aircraft.
     * @param aircraftName The new aircraft name
     */
    public void setAircraftName(String aircraftName) { this.aircraftName.set(aircraftName); }

    /**
     * Gets the JavaFX property for the aircraft name.
     * @return The aircraft name property
     */
    public StringProperty aircraftNameProperty() { return aircraftName; }

    /**
     * Gets the aircraft model.
     * @return The aircraft model
     */
    public String getModel() { return model.get(); }

    /**
     * Sets the aircraft model.
     * @param model The new aircraft model
     */
    public void setModel(String model) { this.model.set(model); }

    /**
     * Gets the JavaFX property for the aircraft model.
     * @return The aircraft model property
     */
    public StringProperty modelProperty() { return model; }

    /**
     * Gets the passenger capacity for the aircraft.
     * @return The aircraft capacity
     */
    public int getCapacity() { return capacity.get(); }

    /**
     * Sets the passenger capacity for the aircraft.
     * @param capacity The new aircraft capacity
     */
    public void setCapacity(int capacity) { this.capacity.set(capacity); }

    /**
     * Gets the JavaFX property for the aircraft capacity.
     * @return The aircraft capacity property
     */
    public IntegerProperty capacityProperty() { return capacity; }
}
