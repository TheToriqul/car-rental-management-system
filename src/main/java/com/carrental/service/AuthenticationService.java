package com.carrental.service;

import com.carrental.dao.UserDAO;
import com.carrental.model.User;
import com.carrental.util.DatabaseManager;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Service class for handling user authentication and session management
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class AuthenticationService {
    
    private User currentUser = null;
    private UserDAO userDAO;
    
    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Authenticate user with username and password
     */
    public boolean authenticate(String username, String password) {
        try {
            // Hash the password for comparison
            String hashedPassword = hashPassword(password);
            
            // Query to check credentials
            String query = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";
            
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Create user object
                        currentUser = new User();
                        currentUser.setId(rs.getInt("id"));
                        currentUser.setUsername(rs.getString("username"));
                        currentUser.setPassword(rs.getString("password"));
                        currentUser.setRole(rs.getString("role"));
                        
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get current authenticated user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get current user role
     */
    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }
    
    /**
     * Check if user is authenticated
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Check if current user is admin
     */
    public boolean isAdmin() {
        return "ADMIN".equals(getCurrentUserRole());
    }
    
    /**
     * Check if current user is staff
     */
    public boolean isStaff() {
        return "STAFF".equals(getCurrentUserRole());
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Hash password using SHA-256
     */
    private String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }
    
    /**
     * Create new user account (Admin only)
     */
    public boolean createUser(String username, String password, String role) {
        if (!isAdmin()) {
            return false;
        }
        
        try {
            String hashedPassword = hashPassword(password);
            return userDAO.createUser(username, hashedPassword, role);
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update user password
     */
    public boolean updatePassword(int userId, String newPassword) {
        try {
            String hashedPassword = hashPassword(newPassword);
            return userDAO.updatePassword(userId, hashedPassword);
        } catch (Exception e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }
}
