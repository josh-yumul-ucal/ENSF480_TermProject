/**
 * ENSF480 Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package util;

import model.User;
import database.DatabaseManager;
import java.sql.*;

/**
 * Utility class providing user authentication and account verification
 * against the database.
 */
public class AuthenticationManager {
    private DatabaseManager dbManager;

    /**
     * Initializes the AuthenticationManager with a database connection manager.
     * @param dbManager The database manager instance
     */
    public AuthenticationManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Authenticates a user by checking their username and password against the database.
     * @param username The user's login username
     * @param password The user's password
     * @return User object if authentication succeeds, null otherwise
     */
    public User authenticateUser(String username, String password) {
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if a username exists in the database.
     * @param username The username to test for existence
     * @return true if username exists, false otherwise
     */
    public boolean checkUserExists(String username) {
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username=?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
