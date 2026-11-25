/**
 * ENSF480 Group 1
 * Final Project: Flight Management System
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
 * Model class representing a user account.
 * Manages user credentials and role-based access control
 * ("Customer", "Flight Agent", "System Administrator")
 */
public class User {
    private final IntegerProperty userId;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty role;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;

    /**
     * Default constructor for creating a new user objects.
     */
    public User() {
        this.userId = new SimpleIntegerProperty();
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    /**
     * Constructor for creating a user object with predefined values.
     * @param userId The unique identifier for the user
     * @param username The username
     * @param password The password
     * @param role The user role that determines the system access permissions
     * @param firstName The first name for the user
     * @param lastName The last name for the user
     * @param email The contact email address for the user
     */
    public User(int userId, String username, String password, String role,
                String firstName, String lastName, String email) {
        this.userId = new SimpleIntegerProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
    }

    // Getters and setters

    /**
     * Gets the unique identifier for the user.
     * @return The user ID
     */
    public int getUserId() { return userId.get(); }

    /**
     * Sets the unique identifier for the user.
     * @param userId The new user ID
     */
    public void setUserId(int userId) { this.userId.set(userId); }

    /**
     * Gets JavaFX property for user ID.
     * @return The user ID property
     */
    public IntegerProperty userIdProperty() { return userId; }

    /**
     * Gets the username.
     * @return The username
     */
    public String getUsername() { return username.get(); }

    /**
     * Sets the username.
     * @param username The new username
     */
    public void setUsername(String username) { this.username.set(username); }

    /**
     * Gets JavaFX property for username.
     * @return The username property
     */
    public StringProperty usernameProperty() { return username; }

    /**
     * Gets the password.
     * @return The password
     */
    public String getPassword() { return password.get(); }

    /**
     * Sets the password.
     * @param password The new password
     */
    public void setPassword(String password) { this.password.set(password); }

    /**
     * Gets JavaFX property for password.
     * @return The password property
     */
    public StringProperty passwordProperty() { return password; }

    /**
     * Gets the user role that determines the system access permissions.
     * @return The user role
     */
    public String getRole() { return role.get(); }

    /**
     * Sets the user role that determines the system access permissions.
     * @param role The new user role
     */
    public void setRole(String role) { this.role.set(role); }

    /**
     * Gets JavaFX property for role.
     * @return The role property
     */
    public StringProperty roleProperty() { return role; }

    /**
     * Gets the first name for the user.
     * @return The first name
     */
    public String getFirstName() { return firstName.get(); }

    /**
     * Sets the first name for the user.
     * @param firstName The new first name
     */
    public void setFirstName(String firstName) { this.firstName.set(firstName); }

    /**
     * Gets JavaFX property for first name.
     * @return The first name property
     */
    public StringProperty firstNameProperty() { return firstName; }

    /**
     * Gets the last name for user.
     * @return The last name
     */
    public String getLastName() { return lastName.get(); }

    /**
     * Sets the last name for the user.
     * @param lastName The new last name
     */
    public void setLastName(String lastName) { this.lastName.set(lastName); }

    /**
     * Gets JavaFX property for last name.
     * @return The last name property
     */
    public StringProperty lastNameProperty() { return lastName; }

    /**
     * Gets the contact email address for the user.
     * @return The email address
     */
    public String getEmail() { return email.get(); }

    /**
     * Sets the contact email address for the user.
     * @param email The new email address
     */
    public void setEmail(String email) { this.email.set(email); }

    /**
     * Gets JavaFX property for email.
     * @return The email property
     */
    public StringProperty emailProperty() { return email; }

    /**
     * Returns a string representation of the user object to perform debugging.
     * @return Formatted string with user details
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
