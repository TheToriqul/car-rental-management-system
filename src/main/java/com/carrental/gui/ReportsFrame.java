package com.carrental.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.carrental.model.Car;
import com.carrental.model.Rental;
import com.carrental.service.CarService;
import com.carrental.service.RentalService;

/**
 * Reports Interface for Admin users
 * Shows rental history, car availability, and revenue statistics
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class ReportsFrame extends JFrame {

  private CarService carService;
  private RentalService rentalService;
  private JTabbedPane tabbedPane;

  public ReportsFrame() {
    this.carService = new CarService();
    this.rentalService = new RentalService();
    initializeComponents();
    setupLayout();
    loadReports();
    setFrameProperties();
  }

  private void initializeComponents() {
    tabbedPane = new JTabbedPane();
  }

  private void setupLayout() {
    setLayout(new BorderLayout());

    // Create gradient background
    JPanel backgroundPanel = ModernUITheme.createGradientPanel(
        ModernUITheme.BACKGROUND_LIGHT,
        ModernUITheme.BACKGROUND_WHITE);
    backgroundPanel.setLayout(new BorderLayout());

    // Header panel with gradient
    JPanel headerPanel = ModernUITheme.createGradientPanel(
        ModernUITheme.ACCENT_COLOR,
        ModernUITheme.PRIMARY_COLOR);
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

    JLabel titleLabel = new JLabel("📊 Reports & Analytics");
    titleLabel.setForeground(ModernUITheme.TEXT_LIGHT);
    titleLabel.setFont(ModernUITheme.FONT_HEADING);
    headerPanel.add(titleLabel, BorderLayout.WEST);

    backgroundPanel.add(headerPanel, BorderLayout.NORTH);

    // Main content with modern tabbed pane
    tabbedPane.setFont(ModernUITheme.FONT_PRIMARY);
    backgroundPanel.add(tabbedPane, BorderLayout.CENTER);

    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
    buttonPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JButton refreshButton = ModernUITheme.createModernButton("🔄 Refresh Reports", "primary");
    JButton backButton = ModernUITheme.createModernButton("⬅️ Back to Dashboard", "danger");

    refreshButton.addActionListener(e -> loadReports());
    backButton.addActionListener(e -> dispose());

    buttonPanel.add(refreshButton);
    buttonPanel.add(backButton);
    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    add(backgroundPanel, BorderLayout.CENTER);
  }

  private void setFrameProperties() {
    setTitle("Reports & Analytics - Car Rental System");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);
    setResizable(true);
  }

  private void loadReports() {
    tabbedPane.removeAll();

    // Add different report tabs
    tabbedPane.addTab("Rental History", createRentalHistoryPanel());
    tabbedPane.addTab("Car Availability", createCarAvailabilityPanel());
    tabbedPane.addTab("Revenue Statistics", createRevenueStatisticsPanel());
    tabbedPane.addTab("System Overview", createSystemOverviewPanel());
  }

  private JPanel createRentalHistoryPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    try {
      List<Rental> rentals = rentalService.getAllRentals();

      // Create table model
      String[] columnNames = { "ID", "Car", "Customer", "Start Date", "End Date", "Total Cost", "Status", "Staff" };
      DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
          return false;
        }
      };

      // Populate table
      for (Rental rental : rentals) {
        String carInfo = rental.getCar() != null
            ? rental.getCar().getMake() + " " + rental.getCar().getModel() + " (" + rental.getCar().getLicensePlate()
                + ")"
            : "Car ID: " + rental.getCarId();

        String customerInfo = rental.getCustomer() != null
            ? rental.getCustomer().getName() + " (" + rental.getCustomer().getEmail() + ")"
            : "Customer ID: " + rental.getCustomerId();

        String staffInfo = rental.getStaff() != null ? rental.getStaff().getUsername()
            : "Staff ID: " + rental.getStaffId();

        Object[] row = {
            rental.getId(),
            carInfo,
            customerInfo,
            rental.getStartDate(),
            rental.getEndDate(),
            "$" + rental.getTotalCost(),
            rental.getStatus(),
            staffInfo
        };
        tableModel.addRow(row);
      }

      JTable table = new JTable(tableModel);
      JScrollPane scrollPane = new JScrollPane(table);

      // Add summary panel
      JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      summaryPanel.add(new JLabel("Total Rentals: " + rentals.size()));

      long activeRentals = rentals.stream().filter(r -> "ACTIVE".equals(r.getStatus())).count();
      long completedRentals = rentals.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();

      summaryPanel.add(new JLabel(" | Active: " + activeRentals));
      summaryPanel.add(new JLabel(" | Completed: " + completedRentals));

      panel.add(summaryPanel, BorderLayout.NORTH);
      panel.add(scrollPane, BorderLayout.CENTER);

    } catch (SQLException e) {
      panel.add(new JLabel("Error loading rental history: " + e.getMessage()), BorderLayout.CENTER);
    }

    return panel;
  }

  private JPanel createCarAvailabilityPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    try {
      List<Car> cars = carService.getAllCars();

      // Create table model
      String[] columnNames = { "ID", "Make", "Model", "Year", "License Plate", "Color", "Status", "Daily Rate" };
      DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
          return false;
        }
      };

      // Populate table
      for (Car car : cars) {
        Object[] row = {
            car.getId(),
            car.getMake(),
            car.getModel(),
            car.getYear(),
            car.getLicensePlate(),
            car.getColor(),
            car.getStatus(),
            "$" + car.getDailyRate()
        };
        tableModel.addRow(row);
      }

      JTable table = new JTable(tableModel);
      JScrollPane scrollPane = new JScrollPane(table);

      // Add summary panel
      JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      summaryPanel.add(new JLabel("Total Cars: " + cars.size()));

      long availableCars = cars.stream().filter(c -> "AVAILABLE".equals(c.getStatus())).count();
      long rentedCars = cars.stream().filter(c -> "RENTED".equals(c.getStatus())).count();
      long maintenanceCars = cars.stream().filter(c -> "MAINTENANCE".equals(c.getStatus())).count();

      summaryPanel.add(new JLabel(" | Available: " + availableCars));
      summaryPanel.add(new JLabel(" | Rented: " + rentedCars));
      summaryPanel.add(new JLabel(" | Maintenance: " + maintenanceCars));

      panel.add(summaryPanel, BorderLayout.NORTH);
      panel.add(scrollPane, BorderLayout.CENTER);

    } catch (SQLException e) {
      panel.add(new JLabel("Error loading car availability: " + e.getMessage()), BorderLayout.CENTER);
    }

    return panel;
  }

  private JPanel createRevenueStatisticsPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    try {
      BigDecimal totalRevenue = rentalService.getTotalRevenue();
      List<Rental> completedRentals = rentalService.getRentalsByStatus("COMPLETED");

      // Create statistics panel
      JPanel statsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
      statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      statsPanel.add(new JLabel("Total Revenue:"));
      statsPanel.add(new JLabel("$" + totalRevenue));

      statsPanel.add(new JLabel("Completed Rentals:"));
      statsPanel.add(new JLabel(String.valueOf(completedRentals.size())));

      statsPanel.add(new JLabel("Average Revenue per Rental:"));
      BigDecimal avgRevenue = completedRentals.isEmpty() ? BigDecimal.ZERO
          : totalRevenue.divide(BigDecimal.valueOf(completedRentals.size()), 2, RoundingMode.HALF_UP);
      statsPanel.add(new JLabel("$" + avgRevenue));

      statsPanel.add(new JLabel("Report Generated:"));
      statsPanel.add(new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

      panel.add(statsPanel, BorderLayout.CENTER);

    } catch (SQLException e) {
      panel.add(new JLabel("Error loading revenue statistics: " + e.getMessage()), BorderLayout.CENTER);
    }

    return panel;
  }

  private JPanel createSystemOverviewPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    try {
      // Get system statistics
      List<Car> cars = carService.getAllCars();
      List<Rental> rentals = rentalService.getAllRentals();
      List<Rental> activeRentals = rentalService.getActiveRentals();
      BigDecimal totalRevenue = rentalService.getTotalRevenue();

      // Create overview panel
      JPanel overviewPanel = new JPanel(new GridLayout(6, 2, 15, 15));
      overviewPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      overviewPanel.add(new JLabel("Total Cars in Fleet:"));
      overviewPanel.add(new JLabel(String.valueOf(cars.size())));

      overviewPanel.add(new JLabel("Available Cars:"));
      long availableCars = cars.stream().filter(c -> "AVAILABLE".equals(c.getStatus())).count();
      overviewPanel.add(new JLabel(String.valueOf(availableCars)));

      overviewPanel.add(new JLabel("Currently Rented:"));
      long rentedCars = cars.stream().filter(c -> "RENTED".equals(c.getStatus())).count();
      overviewPanel.add(new JLabel(String.valueOf(rentedCars)));

      overviewPanel.add(new JLabel("Total Rentals (All Time):"));
      overviewPanel.add(new JLabel(String.valueOf(rentals.size())));

      overviewPanel.add(new JLabel("Active Rentals:"));
      overviewPanel.add(new JLabel(String.valueOf(activeRentals.size())));

      overviewPanel.add(new JLabel("Total Revenue:"));
      overviewPanel.add(new JLabel("$" + totalRevenue));

      panel.add(overviewPanel, BorderLayout.CENTER);

    } catch (SQLException e) {
      panel.add(new JLabel("Error loading system overview: " + e.getMessage()), BorderLayout.CENTER);
    }

    return panel;
  }
}
