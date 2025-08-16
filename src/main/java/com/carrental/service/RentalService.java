package com.carrental.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.carrental.dao.RentalDAO;
import com.carrental.model.Car;
import com.carrental.model.Customer;
import com.carrental.model.Rental;
import com.carrental.util.DatabaseManager;

/**
 * Service class for handling rental-related business logic
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class RentalService {

    private RentalDAO rentalDAO;
    private CarService carService;
    private CustomerService customerService;

    public RentalService() {
        this.rentalDAO = new RentalDAO();
        this.carService = new CarService();
        this.customerService = new CustomerService();
    }

    /**
     * Create a new rental with validation and transaction management
     * Uses database transactions to ensure data consistency
     */
    public boolean createRental(int carId, int customerId, int staffId,
            LocalDate startDate, LocalDate endDate) throws Exception {

        // Validate input
        if (carId <= 0) {
            throw new IllegalArgumentException("Invalid car ID");
        }
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        if (staffId <= 0) {
            throw new IllegalArgumentException("Invalid staff ID");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date is required");
        }

        // Validate dates
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Check if car exists and is available
        Car car = carService.getCarById(carId);
        if (car == null) {
            throw new IllegalArgumentException("Car not found");
        }
        if (!"AVAILABLE".equals(car.getStatus())) {
            throw new IllegalArgumentException("Car is not available for rental");
        }

        // Check if customer exists
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        // Calculate total cost
        long numberOfDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal totalCost = car.getDailyRate().multiply(BigDecimal.valueOf(numberOfDays));

        // Create rental object
        Rental rental = new Rental(carId, customerId, staffId, startDate, endDate, totalCost);

        // Use transaction management for data consistency
        Connection connection = null;
        boolean transactionSuccess = false;

        try {
            // Get connection and start transaction
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Create rental record
            boolean rentalCreated = rentalDAO.createRentalWithConnection(rental, connection);
            if (!rentalCreated) {
                throw new SQLException("Failed to create rental record");
            }

            // Update car status to RENTED
            boolean carStatusUpdated = carService.updateCarStatusWithConnection(carId, "RENTED", connection);
            if (!carStatusUpdated) {
                throw new SQLException("Failed to update car status");
            }

            // If both operations succeed, commit the transaction
            connection.commit();
            transactionSuccess = true;

        } catch (SQLException e) {
            // If any operation fails, rollback the transaction
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            throw new SQLException("Transaction failed: " + e.getMessage(), e);
        } finally {
            // Reset auto-commit and close connection
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }

        return transactionSuccess;
    }

    /**
     * Complete a rental (return car) with transaction management
     * Uses database transactions to ensure data consistency
     */
    public boolean completeRental(int rentalId) throws Exception {
        Rental rental = rentalDAO.getRentalById(rentalId);
        if (rental == null) {
            throw new IllegalArgumentException("Rental not found");
        }

        if (!"ACTIVE".equals(rental.getStatus())) {
            throw new IllegalArgumentException("Rental is not active");
        }

        // Use transaction management for data consistency
        Connection connection = null;
        boolean transactionSuccess = false;

        try {
            // Get connection and start transaction
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Update rental status to COMPLETED
            boolean rentalUpdated = rentalDAO.updateRentalStatusWithConnection(rentalId, "COMPLETED", connection);
            if (!rentalUpdated) {
                throw new SQLException("Failed to update rental status");
            }

            // Update car status to AVAILABLE
            boolean carStatusUpdated = carService.updateCarStatusWithConnection(rental.getCarId(), "AVAILABLE",
                    connection);
            if (!carStatusUpdated) {
                throw new SQLException("Failed to update car status");
            }

            // If both operations succeed, commit the transaction
            connection.commit();
            transactionSuccess = true;

        } catch (SQLException e) {
            // If any operation fails, rollback the transaction
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            throw new SQLException("Transaction failed: " + e.getMessage(), e);
        } finally {
            // Reset auto-commit and close connection
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }

        return transactionSuccess;
    }

    /**
     * Get all rentals
     */
    public List<Rental> getAllRentals() throws SQLException {
        return rentalDAO.getAllRentals();
    }

    /**
     * Get active rentals
     */
    public List<Rental> getActiveRentals() throws SQLException {
        return rentalDAO.getActiveRentals();
    }

    /**
     * Get rentals by status
     */
    public List<Rental> getRentalsByStatus(String status) throws SQLException {
        return rentalDAO.getRentalsByStatus(status);
    }

    /**
     * Get rental by ID
     */
    public Rental getRentalById(int id) throws SQLException {
        return rentalDAO.getRentalById(id);
    }

    /**
     * Get rentals by customer ID
     */
    public List<Rental> getRentalsByCustomerId(int customerId) throws SQLException {
        return rentalDAO.getRentalsByCustomerId(customerId);
    }

    /**
     * Get rentals by car ID
     */
    public List<Rental> getRentalsByCarId(int carId) throws SQLException {
        return rentalDAO.getRentalsByCarId(carId);
    }

    /**
     * Get total revenue
     */
    public BigDecimal getTotalRevenue() throws SQLException {
        return rentalDAO.getTotalRevenue();
    }

    /**
     * Get revenue by date range
     */
    public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        return rentalDAO.getRevenueByDateRange(startDate, endDate);
    }

    /**
     * Get revenue for a specific staff member
     */
    public BigDecimal getRevenueByStaffId(int staffId) throws SQLException {
        return rentalDAO.getRevenueByStaffId(staffId);
    }

    /**
     * Check if car is available for rental
     */
    public boolean isCarAvailable(int carId) throws SQLException {
        Car car = carService.getCarById(carId);
        return car != null && "AVAILABLE".equals(car.getStatus());
    }

    /**
     * Get rental statistics
     */
    public RentalStatistics getRentalStatistics() throws SQLException {
        List<Rental> allRentals = getAllRentals();
        List<Rental> activeRentals = getActiveRentals();
        BigDecimal totalRevenue = getTotalRevenue();

        return new RentalStatistics(
                allRentals.size(),
                activeRentals.size(),
                totalRevenue);
    }

    /**
     * Inner class for rental statistics
     */
    public static class RentalStatistics {
        private int totalRentals;
        private int activeRentals;
        private BigDecimal totalRevenue;

        public RentalStatistics(int totalRentals, int activeRentals, BigDecimal totalRevenue) {
            this.totalRentals = totalRentals;
            this.activeRentals = activeRentals;
            this.totalRevenue = totalRevenue;
        }

        public int getTotalRentals() {
            return totalRentals;
        }

        public int getActiveRentals() {
            return activeRentals;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }
    }
}
