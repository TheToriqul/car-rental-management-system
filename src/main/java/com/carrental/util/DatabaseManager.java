package com.carrental.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Database manager utility class for handling SQLite database operations
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/carrental.db";
    private static final String DB_DIR = "src/main/resources/database";
    private static Connection connection = null;

    /**
     * Initialize the database and create tables if they don't exist
     */
    public static void initializeDatabase() throws SQLException {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }

        // Create database directory if it doesn't exist
        File dbDir = new File(DB_DIR);
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }

        // Create connection
        connection = DriverManager.getConnection(DB_URL);

        // Create tables
        createTables();

        // Insert default data if tables are empty
        insertDefaultData();
    }

    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Create all necessary tables
     */
    private static void createTables() throws SQLException {
        String[] createTableQueries = {
                // Users table
                """
                        CREATE TABLE IF NOT EXISTS users (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username VARCHAR(50) UNIQUE NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'STAFF')),
                            created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                        """,

                // Cars table
                """
                        CREATE TABLE IF NOT EXISTS cars (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            make VARCHAR(50) NOT NULL,
                            model VARCHAR(50) NOT NULL,
                            year INTEGER NOT NULL,
                            license_plate VARCHAR(20) UNIQUE NOT NULL,
                            color VARCHAR(30) NOT NULL,
                            status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
                                CHECK (status IN ('AVAILABLE', 'RENTED', 'MAINTENANCE')),
                            daily_rate DECIMAL(10,2) NOT NULL,
                            created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                        """,

                // Customers table
                """
                        CREATE TABLE IF NOT EXISTS customers (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name VARCHAR(100) NOT NULL,
                            email VARCHAR(100) UNIQUE NOT NULL,
                            phone VARCHAR(20) NOT NULL,
                            address TEXT NOT NULL,
                            created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                        """,

                // Rentals table
                """
                        CREATE TABLE IF NOT EXISTS rentals (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            car_id INTEGER NOT NULL,
                            customer_id INTEGER NOT NULL,
                            staff_id INTEGER NOT NULL,
                            start_date DATE NOT NULL,
                            end_date DATE NOT NULL,
                            total_cost DECIMAL(10,2) NOT NULL,
                            status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                                CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED')),
                            created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (car_id) REFERENCES cars(id),
                            FOREIGN KEY (customer_id) REFERENCES customers(id),
                            FOREIGN KEY (staff_id) REFERENCES users(id)
                        )
                        """
        };

        try (Statement stmt = connection.createStatement()) {
            for (String query : createTableQueries) {
                stmt.execute(query);
            }
        }
    }

    /**
     * Hash password using SHA-256
     */
    private static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    /**
     * Insert default data into tables
     */
    private static void insertDefaultData() throws SQLException {
        // Check if default admin user exists
        String checkAdminQuery = "SELECT COUNT(*) FROM users WHERE username = 'Toriq'";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(checkAdminQuery)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // Insert default admin user with hashed password
                String hashedAdminPassword = hashPassword("toriq123");
                String insertAdminQuery = """
                            INSERT INTO users (username, password, role)
                            VALUES ('Toriq', ?, 'ADMIN')
                        """;
                try (PreparedStatement pstmt = connection.prepareStatement(insertAdminQuery)) {
                    pstmt.setString(1, hashedAdminPassword);
                    pstmt.execute();
                }

                // Insert default staff user with hashed password
                String hashedStaffPassword = hashPassword("jenesh123");
                String insertStaffQuery = """
                            INSERT INTO users (username, password, role)
                            VALUES ('Jenesh', ?, 'STAFF')
                        """;
                try (PreparedStatement pstmt = connection.prepareStatement(insertStaffQuery)) {
                    pstmt.setString(1, hashedStaffPassword);
                    pstmt.execute();
                }

                // Insert sample cars
                String[] sampleCars = {
                        "INSERT INTO cars (make, model, year, license_plate, color, daily_rate) VALUES ('Toyota', 'Camry', 2022, 'ABC123', 'White', 50.00)",
                        "INSERT INTO cars (make, model, year, license_plate, color, daily_rate) VALUES ('Honda', 'Civic', 2021, 'XYZ789', 'Blue', 45.00)",
                        "INSERT INTO cars (make, model, year, license_plate, color, daily_rate) VALUES ('Ford', 'Focus', 2023, 'DEF456', 'Red', 40.00)"
                };

                for (String carQuery : sampleCars) {
                    stmt.execute(carQuery);
                }

                // Insert sample customers
                String[] sampleCustomers = {
                        "INSERT INTO customers (name, email, phone, address) VALUES ('John Doe', 'john@example.com', '123-456-7890', '123 Main St, City')",
                        "INSERT INTO customers (name, email, phone, address) VALUES ('Jane Smith', 'jane@example.com', '098-765-4321', '456 Oak Ave, Town')"
                };

                for (String customerQuery : sampleCustomers) {
                    stmt.execute(customerQuery);
                }

                System.out.println("Default data inserted successfully");
            }
        }
    }
}
