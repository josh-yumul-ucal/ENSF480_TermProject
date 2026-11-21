/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package service;

import model.User;
import dao.UserDAO;
import database.DatabaseManager;
import java.util.List;

/**
 * Service layer for user management.
 * Handles authentication and account management.
 */
public class UserService {
    private UserDAO userDAO;

    /**
     * Initializes the UserService using a database manager.
     * @param dbManager The database manager instance
     */
    public UserService(DatabaseManager dbManager) {
        this.userDAO = new UserDAO(dbManager);
    }

    /**
     * Retrieves a user by their username.
     * This method is used to verify credentials during login.
     * @param username The username to search for
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Retrieves all users from the database.
     * Used in user management panels to display user listings.
     * @return List of all User objects
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Creates a user account in the database.
     * Generates auto-incremented user_id after successfully inserting.
     * @param user The User object containing registration information
     * @return true if user creation was successful, false otherwise
     */
    public boolean addUser(User user) {
        return userDAO.addUser(user);
    }

    /**
     * Updates the information of an existing user in the database.
     * Uses the user_id from the provided User object to identify which record is to be updated.
     * @param user The User object containing updated information
     * @return true if user update was successful, false otherwise
     */
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    /**
     * Deletes a user account.
     * This operation deletes associated user data and can not be undone.
     * @param id The unique identifier for the user that is to be deleted
     * @return true if user deletion was successful, false otherwise
     */
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
}
