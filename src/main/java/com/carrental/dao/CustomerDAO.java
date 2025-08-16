package com.carrental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carrental.model.Customer;
import com.carrental.util.DatabaseManager;

/**
 * Data Access Object for Customer entity
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class CustomerDAO {

  /**
   * Create a new customer
   */
  public boolean createCustomer(Customer customer) throws SQLException {
    String query = "INSERT INTO customers (name, email, phone, address) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, customer.getName());
      pstmt.setString(2, customer.getEmail());
      pstmt.setString(3, customer.getPhone());
      pstmt.setString(4, customer.getAddress());

      int affectedRows = pstmt.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
          if (rs.next()) {
            customer.setId(rs.getInt(1));
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Get customer by ID
   */
  public Customer getCustomerById(int id) throws SQLException {
    String query = "SELECT * FROM customers WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToCustomer(rs);
        }
      }
    }

    return null;
  }

  /**
   * Get customer by email
   */
  public Customer getCustomerByEmail(String email) throws SQLException {
    String query = "SELECT * FROM customers WHERE email = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, email);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToCustomer(rs);
        }
      }
    }

    return null;
  }

  /**
   * Get all customers
   */
  public List<Customer> getAllCustomers() throws SQLException {
    List<Customer> customers = new ArrayList<>();
    String query = "SELECT * FROM customers ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        customers.add(mapResultSetToCustomer(rs));
      }
    }

    return customers;
  }

  /**
   * Search customers by name, email, or phone
   */
  public List<Customer> searchCustomers(String searchTerm) throws SQLException {
    List<Customer> customers = new ArrayList<>();
    String query = "SELECT * FROM customers WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      String searchPattern = "%" + searchTerm + "%";
      pstmt.setString(1, searchPattern);
      pstmt.setString(2, searchPattern);
      pstmt.setString(3, searchPattern);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          customers.add(mapResultSetToCustomer(rs));
        }
      }
    }

    return customers;
  }

  /**
   * Update customer
   */
  public boolean updateCustomer(Customer customer) throws SQLException {
    String query = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, customer.getName());
      pstmt.setString(2, customer.getEmail());
      pstmt.setString(3, customer.getPhone());
      pstmt.setString(4, customer.getAddress());
      pstmt.setInt(5, customer.getId());

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Delete customer
   */
  public boolean deleteCustomer(int id) throws SQLException {
    String query = "DELETE FROM customers WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, id);

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Check if email exists
   */
  public boolean emailExists(String email) throws SQLException {
    String query = "SELECT COUNT(*) FROM customers WHERE email = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, email);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1) > 0;
        }
      }
    }

    return false;
  }

  /**
   * Map ResultSet to Customer object
   */
  private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
    Customer customer = new Customer();
    customer.setId(rs.getInt("id"));
    customer.setName(rs.getString("name"));
    customer.setEmail(rs.getString("email"));
    customer.setPhone(rs.getString("phone"));
    customer.setAddress(rs.getString("address"));

    Timestamp createdDate = rs.getTimestamp("created_date");
    if (createdDate != null) {
      customer.setCreatedDate(createdDate.toLocalDateTime());
    }

    return customer;
  }
}
