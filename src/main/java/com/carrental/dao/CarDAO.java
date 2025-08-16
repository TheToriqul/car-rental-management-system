package com.carrental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carrental.model.Car;
import com.carrental.util.DatabaseManager;

/**
 * Data Access Object for Car entity
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class CarDAO {

  /**
   * Create a new car
   */
  public boolean createCar(Car car) throws SQLException {
    String query = "INSERT INTO cars (make, model, year, license_plate, color, status, daily_rate) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, car.getMake());
      pstmt.setString(2, car.getModel());
      pstmt.setInt(3, car.getYear());
      pstmt.setString(4, car.getLicensePlate());
      pstmt.setString(5, car.getColor());
      pstmt.setString(6, car.getStatus());
      pstmt.setBigDecimal(7, car.getDailyRate());

      int affectedRows = pstmt.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
          if (rs.next()) {
            car.setId(rs.getInt(1));
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Get car by ID
   */
  public Car getCarById(int id) throws SQLException {
    String query = "SELECT * FROM cars WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToCar(rs);
        }
      }
    }

    return null;
  }

  /**
   * Get car by license plate
   */
  public Car getCarByLicensePlate(String licensePlate) throws SQLException {
    String query = "SELECT * FROM cars WHERE license_plate = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, licensePlate);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToCar(rs);
        }
      }
    }

    return null;
  }

  /**
   * Get all cars
   */
  public List<Car> getAllCars() throws SQLException {
    List<Car> cars = new ArrayList<>();
    String query = "SELECT * FROM cars ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        cars.add(mapResultSetToCar(rs));
      }
    }

    return cars;
  }

  /**
   * Get cars by status
   */
  public List<Car> getCarsByStatus(String status) throws SQLException {
    List<Car> cars = new ArrayList<>();
    String query = "SELECT * FROM cars WHERE status = ? ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, status);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          cars.add(mapResultSetToCar(rs));
        }
      }
    }

    return cars;
  }

  /**
   * Get available cars
   */
  public List<Car> getAvailableCars() throws SQLException {
    return getCarsByStatus("AVAILABLE");
  }

  /**
   * Search cars by make, model, or license plate
   */
  public List<Car> searchCars(String searchTerm) throws SQLException {
    List<Car> cars = new ArrayList<>();
    String query = "SELECT * FROM cars WHERE make LIKE ? OR model LIKE ? OR license_plate LIKE ? ORDER BY created_date DESC";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      String searchPattern = "%" + searchTerm + "%";
      pstmt.setString(1, searchPattern);
      pstmt.setString(2, searchPattern);
      pstmt.setString(3, searchPattern);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          cars.add(mapResultSetToCar(rs));
        }
      }
    }

    return cars;
  }

  /**
   * Update car
   */
  public boolean updateCar(Car car) throws SQLException {
    String query = "UPDATE cars SET make = ?, model = ?, year = ?, license_plate = ?, color = ?, status = ?, daily_rate = ? WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, car.getMake());
      pstmt.setString(2, car.getModel());
      pstmt.setInt(3, car.getYear());
      pstmt.setString(4, car.getLicensePlate());
      pstmt.setString(5, car.getColor());
      pstmt.setString(6, car.getStatus());
      pstmt.setBigDecimal(7, car.getDailyRate());
      pstmt.setInt(8, car.getId());

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Update car status
   */
  public boolean updateCarStatus(int carId, String status) throws SQLException {
    String query = "UPDATE cars SET status = ? WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, status);
      pstmt.setInt(2, carId);

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Delete car
   */
  public boolean deleteCar(int id) throws SQLException {
    String query = "DELETE FROM cars WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, id);

      return pstmt.executeUpdate() > 0;
    }
  }

  /**
   * Check if license plate exists
   */
  public boolean licensePlateExists(String licensePlate) throws SQLException {
    String query = "SELECT COUNT(*) FROM cars WHERE license_plate = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, licensePlate);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1) > 0;
        }
      }
    }

    return false;
  }

  /**
   * Map ResultSet to Car object
   */
  private Car mapResultSetToCar(ResultSet rs) throws SQLException {
    Car car = new Car();
    car.setId(rs.getInt("id"));
    car.setMake(rs.getString("make"));
    car.setModel(rs.getString("model"));
    car.setYear(rs.getInt("year"));
    car.setLicensePlate(rs.getString("license_plate"));
    car.setColor(rs.getString("color"));
    car.setStatus(rs.getString("status"));
    car.setDailyRate(rs.getBigDecimal("daily_rate"));

    Timestamp createdDate = rs.getTimestamp("created_date");
    if (createdDate != null) {
      car.setCreatedDate(createdDate.toLocalDateTime());
    }

    return car;
  }
}
