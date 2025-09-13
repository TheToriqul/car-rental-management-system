package com.carrental.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.carrental.model.Car;
import com.carrental.service.CarService;

/**
 * Car Management Frame for Admin users
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class CarManagementFrame extends JFrame {

  private CarService carService;
  private JTable carTable;
  private DefaultTableModel tableModel;
  private JTextField searchField;
  private JComboBox<String> statusFilter;

  public CarManagementFrame() {
    this.carService = new CarService();
    initializeComponents();
    setupLayout();
    setupEventHandlers();
    loadCars();
    setFrameProperties();
  }

  private void initializeComponents() {
    // Initialize table model
    String[] columnNames = { "ID", "Make", "Model", "Year", "License Plate", "Color", "Status", "Daily Rate",
        "Created Date" };
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Make table read-only
      }
    };

    carTable = new JTable(tableModel);
    carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    carTable.getTableHeader().setReorderingAllowed(false);

    // Apply modern styling to table
    carTable.setFont(ModernUITheme.FONT_PRIMARY);
    carTable.setRowHeight(30);
    carTable.setGridColor(ModernUITheme.BORDER_LIGHT);
    carTable.getTableHeader().setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    carTable.getTableHeader().setBackground(ModernUITheme.PRIMARY_COLOR);
    carTable.getTableHeader().setForeground(ModernUITheme.TEXT_LIGHT);

    // Initialize search and filter components
    searchField = new JTextField(20);
    statusFilter = new JComboBox<>(new String[] { "All", "Available", "Rented", "Maintenance" });

    // Apply modern styling
    ModernUITheme.styleComponent(searchField);
    ModernUITheme.styleComponent(statusFilter);
    searchField.setBorder(ModernUITheme.BORDER_ROUNDED);
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
        ModernUITheme.PRIMARY_COLOR,
        ModernUITheme.PRIMARY_DARK);
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

    JLabel titleLabel = new JLabel("🚗 Car Management");
    titleLabel.setForeground(ModernUITheme.TEXT_LIGHT);
    titleLabel.setFont(ModernUITheme.FONT_HEADING);
    headerPanel.add(titleLabel, BorderLayout.WEST);

    backgroundPanel.add(headerPanel, BorderLayout.NORTH);

    // Search and filter panel
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    searchPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JLabel searchLabel = new JLabel("Search:");
    searchLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    searchLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
    searchPanel.add(searchLabel);
    searchPanel.add(searchField);

    JLabel statusLabel = new JLabel("Status:");
    statusLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    statusLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
    searchPanel.add(statusLabel);
    searchPanel.add(statusFilter);

    JButton searchButton = ModernUITheme.createModernButton("🔍 Search", "primary");
    JButton clearButton = ModernUITheme.createModernButton("🗑️ Clear", "danger");
    searchPanel.add(searchButton);
    searchPanel.add(clearButton);

    // Table panel
    JScrollPane scrollPane = new JScrollPane(carTable);
    scrollPane.setPreferredSize(new Dimension(800, 400));
    scrollPane.setBorder(BorderFactory.createLineBorder(ModernUITheme.BORDER_LIGHT, 1));

    // Main content panel
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    mainPanel.add(searchPanel, BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    backgroundPanel.add(mainPanel, BorderLayout.CENTER);

    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
    buttonPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JButton addButton = ModernUITheme.createModernButton("➕ Add Car", "success");
    JButton editButton = ModernUITheme.createModernButton("✏️ Edit Car", "primary");
    JButton deleteButton = ModernUITheme.createModernButton("🗑️ Delete Car", "danger");
    JButton refreshButton = ModernUITheme.createModernButton("🔄 Refresh", "primary");
    JButton backButton = ModernUITheme.createModernButton("⬅️ Back to Dashboard", "danger");

    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(refreshButton);
    buttonPanel.add(backButton);

    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    add(backgroundPanel, BorderLayout.CENTER);

    // Add event handlers
    searchButton.addActionListener(e -> performSearch());
    clearButton.addActionListener(e -> {
      searchField.setText("");
      statusFilter.setSelectedItem("All");
      loadCars();
    });
    addButton.addActionListener(e -> addNewCar());
    editButton.addActionListener(e -> editSelectedCar());
    deleteButton.addActionListener(e -> deleteSelectedCar());
    refreshButton.addActionListener(e -> loadCars());
    backButton.addActionListener(e -> dispose());
  }

  private void setupEventHandlers() {
    // Search field enter key
    searchField.addActionListener(e -> performSearch());

    // Status filter change
    statusFilter.addActionListener(e -> performSearch());

    // Table double-click to edit
    carTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          editSelectedCar();
        }
      }
    });
  }

  private void setFrameProperties() {
    setTitle("Car Management - Car Rental System");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);
    setResizable(true);
  }

  private void loadCars() {
    try {
      List<Car> cars = carService.getAllCars();
      updateTableData(cars);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading cars: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void updateTableData(List<Car> cars) {
    tableModel.setRowCount(0);
    for (Car car : cars) {
      Object[] row = {
          car.getId(),
          car.getMake(),
          car.getModel(),
          car.getYear(),
          car.getLicensePlate(),
          car.getColor(),
          car.getStatus(),
          "$" + car.getDailyRate(),
          car.getCreatedDate() != null ? car.getCreatedDate().toString() : ""
      };
      tableModel.addRow(row);
    }
  }

  private void performSearch() {
    try {
      String searchTerm = searchField.getText().trim();
      String statusFilterValue = (String) statusFilter.getSelectedItem();

      List<Car> cars;
      if (searchTerm.isEmpty() && "All".equals(statusFilterValue)) {
        cars = carService.getAllCars();
      } else {
        cars = carService.searchCars(searchTerm);

        // Apply status filter
        if (!"All".equals(statusFilterValue)) {
          cars = cars.stream()
              .filter(car -> car.getStatus().equals(statusFilterValue.toUpperCase()))
              .toList();
        }
      }

      updateTableData(cars);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error searching cars: " + e.getMessage(),
          "Search Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void addNewCar() {
    CarDialog dialog = new CarDialog(this, null);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      loadCars(); // Refresh table
    }
  }

  private void editSelectedCar() {
    int selectedRow = carTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a car to edit.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int carId = (Integer) tableModel.getValueAt(selectedRow, 0);
    try {
      Car car = carService.getCarById(carId);
      if (car != null) {
        CarDialog dialog = new CarDialog(this, car);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
          loadCars(); // Refresh table
        }
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading car details: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelectedCar() {
    int selectedRow = carTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a car to delete.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int carId = (Integer) tableModel.getValueAt(selectedRow, 0);
    String carInfo = tableModel.getValueAt(selectedRow, 1) + " " +
        tableModel.getValueAt(selectedRow, 2) + " (" +
        tableModel.getValueAt(selectedRow, 4) + ")";

    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete this car?\n" + carInfo,
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        boolean deleted = carService.deleteCar(carId);
        if (deleted) {
          JOptionPane.showMessageDialog(this,
              "Car deleted successfully.",
              "Success",
              JOptionPane.INFORMATION_MESSAGE);
          loadCars(); // Refresh table
        } else {
          JOptionPane.showMessageDialog(this,
              "Failed to delete car.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error deleting car: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Inner class for car dialog (add/edit)
   */
  private class CarDialog extends JDialog {
    private Car car;
    private boolean confirmed = false;

    private JTextField makeField, modelField, yearField, licensePlateField, colorField, dailyRateField;
    private JComboBox<String> statusCombo;

    public CarDialog(JFrame parent, Car car) {
      super(parent, car == null ? "Add New Car" : "Edit Car", true);
      this.car = car;
      initializeDialog();
    }

    private void initializeDialog() {
      setLayout(new BorderLayout());

      // Create gradient background
      JPanel backgroundPanel = ModernUITheme.createGradientPanel(
          ModernUITheme.BACKGROUND_LIGHT,
          ModernUITheme.BACKGROUND_WHITE);
      backgroundPanel.setLayout(new BorderLayout());

      // Header panel
      JPanel headerPanel = ModernUITheme.createGradientPanel(
          ModernUITheme.PRIMARY_COLOR,
          ModernUITheme.PRIMARY_DARK);
      headerPanel.setLayout(new BorderLayout());
      headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

      JLabel titleLabel = new JLabel(car == null ? "➕ Add New Car" : "✏️ Edit Car");
      titleLabel.setForeground(ModernUITheme.TEXT_LIGHT);
      titleLabel.setFont(ModernUITheme.FONT_HEADING);
      headerPanel.add(titleLabel, BorderLayout.CENTER);

      backgroundPanel.add(headerPanel, BorderLayout.NORTH);

      // Form panel
      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
      formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(8, 8, 8, 8);
      gbc.anchor = GridBagConstraints.WEST;

      // Initialize fields with modern styling
      makeField = new JTextField(20);
      modelField = new JTextField(20);
      yearField = new JTextField(20);
      licensePlateField = new JTextField(20);
      colorField = new JTextField(20);
      dailyRateField = new JTextField(20);
      statusCombo = new JComboBox<>(new String[] { "AVAILABLE", "RENTED", "MAINTENANCE" });

      // Apply modern styling to fields
      ModernUITheme.styleComponent(makeField);
      ModernUITheme.styleComponent(modelField);
      ModernUITheme.styleComponent(yearField);
      ModernUITheme.styleComponent(licensePlateField);
      ModernUITheme.styleComponent(colorField);
      ModernUITheme.styleComponent(dailyRateField);
      ModernUITheme.styleComponent(statusCombo);

      makeField.setBorder(ModernUITheme.BORDER_ROUNDED);
      modelField.setBorder(ModernUITheme.BORDER_ROUNDED);
      yearField.setBorder(ModernUITheme.BORDER_ROUNDED);
      licensePlateField.setBorder(ModernUITheme.BORDER_ROUNDED);
      colorField.setBorder(ModernUITheme.BORDER_ROUNDED);
      dailyRateField.setBorder(ModernUITheme.BORDER_ROUNDED);

      // Add components to form with modern labels
      gbc.gridx = 0;
      gbc.gridy = 0;
      JLabel makeLabel = new JLabel("Make:");
      makeLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
      makeLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
      formPanel.add(makeLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(makeField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 1;
      JLabel modelLabel = new JLabel("Model:");
      modelLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
      modelLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
      formPanel.add(modelLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(modelField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 2;
      JLabel yearLabel = new JLabel("Year:");
      yearLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
      yearLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
      formPanel.add(yearLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(yearField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 3;
      JLabel licenseLabel = new JLabel("License Plate:");
      licenseLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
      licenseLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
      formPanel.add(licenseLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(licensePlateField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 4;
      JLabel colorLabel = new JLabel("Color:");
      colorLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
      colorLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
      formPanel.add(colorLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(colorField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 5;
      JLabel rateLabel = new JLabel("Daily Rate:");
      rateLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
      rateLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
      formPanel.add(rateLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(dailyRateField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 6;
      JLabel statusLabel = new JLabel("Status:");
      statusLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
      statusLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
      formPanel.add(statusLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(statusCombo, gbc);

      backgroundPanel.add(formPanel, BorderLayout.CENTER);

      // Button panel
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
      buttonPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

      JButton saveButton = ModernUITheme.createModernButton("💾 Save", "success");
      JButton cancelButton = ModernUITheme.createModernButton("❌ Cancel", "danger");

      saveButton.addActionListener(e -> saveCar());
      cancelButton.addActionListener(e -> dispose());

      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
      add(backgroundPanel, BorderLayout.CENTER);

      // Load car data if editing
      if (car != null) {
        loadCarData();
      }

      setSize(500, 650);
      setLocationRelativeTo(getParent());
    }

    private void loadCarData() {
      makeField.setText(car.getMake());
      modelField.setText(car.getModel());
      yearField.setText(String.valueOf(car.getYear()));
      licensePlateField.setText(car.getLicensePlate());
      colorField.setText(car.getColor());
      dailyRateField.setText(car.getDailyRate().toString());
      statusCombo.setSelectedItem(car.getStatus());
    }

    private void saveCar() {
      try {
        String make = makeField.getText().trim();
        String model = modelField.getText().trim();
        int year = Integer.parseInt(yearField.getText().trim());
        String licensePlate = licensePlateField.getText().trim();
        String color = colorField.getText().trim();
        BigDecimal dailyRate = new BigDecimal(dailyRateField.getText().trim());
        String status = (String) statusCombo.getSelectedItem();

        if (car == null) {
          // Add new car
          carService.createCar(make, model, year, licensePlate, color, dailyRate);
        } else {
          // Update existing car
          car.setMake(make);
          car.setModel(model);
          car.setYear(year);
          car.setLicensePlate(licensePlate);
          car.setColor(color);
          car.setDailyRate(dailyRate);
          car.setStatus(status);
          carService.updateCar(car);
        }

        confirmed = true;
        dispose();

      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "Please enter valid numbers for Year and Daily Rate.",
            "Invalid Input",
            JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error saving car: " + e.getMessage(),
            "Save Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }

    public boolean isConfirmed() {
      return confirmed;
    }
  }
}
