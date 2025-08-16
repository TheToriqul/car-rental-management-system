package com.carrental.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.carrental.dao.CarDAO;
import com.carrental.model.Car;

/**
 * Service class for handling car-related business logic
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class CarService {

    private CarDAO carDAO;

    public CarService() {
        this.carDAO = new CarDAO();
    }

    /**
     * Create a new car with validation
     */
    public boolean createCar(String make, String model, int year, String licensePlate,
            String color, BigDecimal dailyRate) throws Exception {

        // Validate input
        if (make == null || make.trim().isEmpty()) {
            throw new IllegalArgumentException("Make is required");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model is required");
        }
        if (year < 1900 || year > 2030) {
            throw new IllegalArgumentException("Year must be between 1900 and 2030");
        }
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate is required");
        }
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Color is required");
        }
        if (dailyRate == null || dailyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Daily rate must be greater than 0");
        }

        // Check if license plate already exists
        if (carDAO.licensePlateExists(licensePlate.trim())) {
            throw new IllegalArgumentException("License plate already exists");
        }

        // Create car object
        Car car = new Car(make.trim(), model.trim(), year, licensePlate.trim(),
                color.trim(), dailyRate);

        return carDAO.createCar(car);
    }

    /**
     * Get all cars
     */
    public List<Car> getAllCars() throws SQLException {
        return carDAO.getAllCars();
    }

    /**
     * Get available cars
     */
    public List<Car> getAvailableCars() throws SQLException {
        return carDAO.getAvailableCars();
    }

    /**
     * Get car by ID
     */
    public Car getCarById(int id) throws SQLException {
        return carDAO.getCarById(id);
    }

    /**
     * Search cars
     */
    public List<Car> searchCars(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllCars();
        }
        return carDAO.searchCars(searchTerm.trim());
    }

    /**
     * Update car
     */
    public boolean updateCar(Car car) throws Exception {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }

        // Validate required fields
        if (car.getMake() == null || car.getMake().trim().isEmpty()) {
            throw new IllegalArgumentException("Make is required");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Model is required");
        }
        if (car.getLicensePlate() == null || car.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("License plate is required");
        }
        if (car.getDailyRate() == null || car.getDailyRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Daily rate must be greater than 0");
        }

        return carDAO.updateCar(car);
    }

    /**
     * Update car status
     */
    public boolean updateCarStatus(int carId, String status) throws SQLException {
        if (status == null
                || (!status.equals("AVAILABLE") && !status.equals("RENTED") && !status.equals("MAINTENANCE"))) {
            throw new IllegalArgumentException("Invalid status. Must be AVAILABLE, RENTED, or MAINTENANCE");
        }

        return carDAO.updateCarStatus(carId, status);
    }

    /**
     * Update car status with provided connection (for transaction management)
     */
    public boolean updateCarStatusWithConnection(int carId, String status, Connection connection) throws SQLException {
        return carDAO.updateCarStatusWithConnection(carId, status, connection);
    }

    /**
     * Delete car
     */
    public boolean deleteCar(int id) throws SQLException {
        return carDAO.deleteCar(id);
    }

    /**
     * Check if car exists
     */
    public boolean carExists(int id) throws SQLException {
        return carDAO.getCarById(id) != null;
    }
}
