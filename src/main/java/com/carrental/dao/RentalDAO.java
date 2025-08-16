package com.carrental.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.carrental.model.Rental;
import com.carrental.util.DatabaseManager;

/**
 * Data Access Object for Rental entity
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class RentalDAO {

  /**
   * Create a new rental
   */
  public boolean createRental(Rental rental) throws SQLException {
    String query = "INSERT INTO rentals (car_id, customer_id, staff_id, start_date, end_date, total_cost, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, rental.getCarId());
      pstmt.setInt(2, rental.getCustomerId());
      pstmt.setInt(3, rental.getStaffId());
      pstmt.setDate(4, java.sql.Date.valueOf(rental.getStartDate()));
      pstmt.setDate(5, java.sql.Date.valueOf(rental.getEndDate()));
      pstmt.setBigDecimal(6, rental.getTotalCost());
      pstmt.setString(7, rental.getStatus());

      int affectedRows = pstmt.executeUpdate();

      if (affectedRows > 0) {
        // Get the generated ID using last_insert_rowid() for SQLite
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
          if (rs.next()) {
            rental.setId(rs.getInt(1));
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Create a new rental with provided connection (for transaction management)
   */
  public boolean createRentalWithConnection(Rental rental, Connection connection) throws SQLException {
    String query = "INSERT INTO rentals (car_id, customer_id, staff_id, start_date, end_date, total_cost, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {

      pstmt.setInt(1, rental.getCarId());
      pstmt.setInt(2, rental.getCustomerId());
      pstmt.setInt(3, rental.getStaffId());
      pstmt.setDate(4, java.sql.Date.valueOf(rental.getStartDate()));
      pstmt.setDate(5, java.sql.Date.valueOf(rental.getEndDate()));
      pstmt.setBigDecimal(6, rental.getTotalCost());
      pstmt.setString(7, rental.getStatus());

      int affectedRows = pstmt.executeUpdate();

      if (affectedRows > 0) {
        // Get the generated ID using last_insert_rowid() for SQLite
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
          if (rs.next()) {
            rental.setId(rs.getInt(1));
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Get rental by ID
   */
  public Rental getRentalById(int id) throws SQLException {
    String query = "SELECT * FROM rentals WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToRental(rs);
        }
      }
    }

    return null;
  }

  /**
   * Get all rentals
   */
  public List<Rental> getAllRentals() throws SQLException {
    List<Rental> rentals = new ArrayList<>();
    String query = "SELECT * FROM rentals ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        rentals.add(mapResultSetToRental(rs));
      }
    }

    return rentals;
  }

  /**
   * Get rentals by status
   */
  public List<Rental> getRentalsByStatus(String status) throws SQLException {
    List<Rental> rentals = new ArrayList<>();
    String query = "SELECT * FROM rentals WHERE status = ? ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, status);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          rentals.add(mapResultSetToRental(rs));
        }
      }
    }

    return rentals;
  }

  /**
   * Get active rentals
   */
  public List<Rental> getActiveRentals() throws SQLException {
    return getRentalsByStatus("ACTIVE");
  }

  /**
   * Get rentals by customer ID
   */
  public List<Rental> getRentalsByCustomerId(int customerId) throws SQLException {
    List<Rental> rentals = new ArrayList<>();
    String query = "SELECT * FROM rentals WHERE customer_id = ? ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, customerId);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          rentals.add(mapResultSetToRental(rs));
        }
      }
    }

    return rentals;
  }

  /**
   * Get rentals by car ID
   */
  public List<Rental> getRentalsByCarId(int carId) throws SQLException {
    List<Rental> rentals = new ArrayList<>();
    String query = "SELECT * FROM rentals WHERE car_id = ? ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, carId);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          rentals.add(mapResultSetToRental(rs));
        }
      }
    }

    return rentals;
  }

  /**
   * Get rentals by date range
   */
  public List<Rental> getRentalsByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
    List<Rental> rentals = new ArrayList<>();
    String query = "SELECT * FROM rentals WHERE (start_date BETWEEN ? AND ?) OR (end_date BETWEEN ? AND ?) ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setDate(1, java.sql.Date.valueOf(startDate));
      pstmt.setDate(2, java.sql.Date.valueOf(endDate));
      pstmt.setDate(3, java.sql.Date.valueOf(startDate));
      pstmt.setDate(4, java.sql.Date.valueOf(endDate));

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          rentals.add(mapResultSetToRental(rs));
        }
      }
    }

    return rentals;
  }

  /**
   * Update rental
   */
  public boolean updateRental(Rental rental) throws SQLException {
    String query = "UPDATE rentals SET car_id = ?, customer_id = ?, staff_id = ?, start_date = ?, end_date = ?, total_cost = ?, status = ? WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, rental.getCarId());
      pstmt.setInt(2, rental.getCustomerId());
      pstmt.setInt(3, rental.getStaffId());
      pstmt.setDate(4, java.sql.Date.valueOf(rental.getStartDate()));
      pstmt.setDate(5, java.sql.Date.valueOf(rental.getEndDate()));
      pstmt.setBigDecimal(6, rental.getTotalCost());
      pstmt.setString(7, rental.getStatus());
      pstmt.setInt(8, rental.getId());

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Update rental status
   */
  public boolean updateRentalStatus(int rentalId, String status) throws SQLException {
    String query = "UPDATE rentals SET status = ? WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, status);
      pstmt.setInt(2, rentalId);

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Update rental status with provided connection (for transaction management)
   */
  public boolean updateRentalStatusWithConnection(int rentalId, String status, Connection connection)
      throws SQLException {
    String query = "UPDATE rentals SET status = ? WHERE id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {

      pstmt.setString(1, status);
      pstmt.setInt(2, rentalId);

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Delete rental
   */
  public boolean deleteRental(int id) throws SQLException {
    String query = "DELETE FROM rentals WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, id);

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Get total revenue
   */
  public BigDecimal getTotalRevenue() throws SQLException {
    String query = "SELECT SUM(total_cost) FROM rentals WHERE status = 'COMPLETED'";

    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      if (rs.next()) {
        BigDecimal revenue = rs.getBigDecimal(1);
        return revenue != null ? revenue : BigDecimal.ZERO;
      }
    }

    return BigDecimal.ZERO;
  }

  /**
   * Get revenue by date range
   */
  public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
    String query = "SELECT SUM(total_cost) FROM rentals WHERE status = 'COMPLETED' AND created_date BETWEEN ? AND ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setDate(1, java.sql.Date.valueOf(startDate));
      pstmt.setDate(2, java.sql.Date.valueOf(endDate));

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          BigDecimal revenue = rs.getBigDecimal(1);
          return revenue != null ? revenue : BigDecimal.ZERO;
        }
      }
    }

    return BigDecimal.ZERO;
  }

  /**
   * Get revenue for a specific staff member
   */
  public BigDecimal getRevenueByStaffId(int staffId) throws SQLException {
    String query = "SELECT SUM(total_cost) FROM rentals WHERE status = 'COMPLETED' AND staff_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, staffId);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          BigDecimal revenue = rs.getBigDecimal(1);
          return revenue != null ? revenue : BigDecimal.ZERO;
        }
      }
    }

    return BigDecimal.ZERO;
  }

  /**
   * Map ResultSet to Rental object
   */
  private Rental mapResultSetToRental(ResultSet rs) throws SQLException {
    Rental rental = new Rental();
    rental.setId(rs.getInt("id"));
    rental.setCarId(rs.getInt("car_id"));
    rental.setCustomerId(rs.getInt("customer_id"));
    rental.setStaffId(rs.getInt("staff_id"));

    Date startDate = rs.getDate("start_date");
    if (startDate != null) {
      rental.setStartDate(startDate.toLocalDate());
    }

    Date endDate = rs.getDate("end_date");
    if (endDate != null) {
      rental.setEndDate(endDate.toLocalDate());
    }

    rental.setTotalCost(rs.getBigDecimal("total_cost"));
    rental.setStatus(rs.getString("status"));

    Timestamp createdDate = rs.getTimestamp("created_date");
    if (createdDate != null) {
      rental.setCreatedDate(createdDate.toLocalDateTime());
    }

    // Don't load related objects here to avoid statement pointer issues
    // They will be loaded separately when needed

    return rental;
  }
}
