package com.carrental.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.carrental.dao.UserDAO;
import com.carrental.model.User;

/**
 * Service class for user management operations
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class UserService {

  private UserDAO userDAO;

  public UserService() {
    this.userDAO = new UserDAO();
  }

  /**
   * Create a new user with validation
   */
  public boolean createUser(String username, String password, String role) throws Exception {
    // Validate input
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username is required");
    }

    if (password == null || password.trim().isEmpty()) {
      throw new IllegalArgumentException("Password is required");
    }

    if (role == null || role.trim().isEmpty()) {
      throw new IllegalArgumentException("Role is required");
    }

    // Validate role
    if (!"ADMIN".equals(role) && !"STAFF".equals(role)) {
      throw new IllegalArgumentException("Role must be ADMIN or STAFF");
    }

    // Check if username already exists
    if (userDAO.usernameExists(username)) {
      throw new IllegalArgumentException("Username already exists");
    }

    // Hash password
    String hashedPassword = DigestUtils.sha256Hex(password);

    return userDAO.createUser(username, hashedPassword, role);
  }

  /**
   * Get all users
   */
  public List<User> getAllUsers() throws SQLException {
    return userDAO.getAllUsers();
  }

  /**
   * Get user by ID
   */
  public User getUserById(int id) throws SQLException {
    return userDAO.getUserById(id);
  }

  /**
   * Get user by username
   */
  public User getUserByUsername(String username) throws SQLException {
    return userDAO.getUserByUsername(username);
  }

  /**
   * Search users by username or role
   */
  public List<User> searchUsers(String searchTerm) throws SQLException {
    return userDAO.searchUsers(searchTerm);
  }

  /**
   * Update user
   */
  public boolean updateUser(User user) throws SQLException {
    // Validate input
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }

    if (user.getId() <= 0) {
      throw new IllegalArgumentException("Invalid user ID");
    }

    if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
      throw new IllegalArgumentException("Username is required");
    }

    if (user.getRole() == null || user.getRole().trim().isEmpty()) {
      throw new IllegalArgumentException("Role is required");
    }

    // Validate role
    if (!"ADMIN".equals(user.getRole()) && !"STAFF".equals(user.getRole())) {
      throw new IllegalArgumentException("Role must be ADMIN or STAFF");
    }

    boolean updated = userDAO.updateUser(user);

    // If password was updated, update it separately
    if (updated && user.getPassword() != null && !user.getPassword().isEmpty()) {
      userDAO.updatePassword(user.getId(), user.getPassword());
    }

    return updated;
  }

  /**
   * Delete user
   */
  public boolean deleteUser(int userId) throws SQLException {
    // Validate input
    if (userId <= 0) {
      throw new IllegalArgumentException("Invalid user ID");
    }

    // Get user to check if it's admin
    User user = userDAO.getUserById(userId);
    if (user != null && "ADMIN".equals(user.getRole())) {
      throw new IllegalArgumentException("Cannot delete admin user");
    }

    return userDAO.deleteUser(userId);
  }

  /**
   * Check if username exists
   */
  public boolean usernameExists(String username) throws SQLException {
    return userDAO.usernameExists(username);
  }

  /**
   * Get users by role
   */
  public List<User> getUsersByRole(String role) throws SQLException {
    return userDAO.getUsersByRole(role);
  }
}
