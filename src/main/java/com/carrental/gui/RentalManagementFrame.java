package com.carrental.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import com.carrental.model.Car;
import com.carrental.model.Customer;
import com.carrental.model.Rental;
import com.carrental.model.User;
import com.carrental.service.AuthenticationService;
import com.carrental.service.CarService;
import com.carrental.service.CustomerService;
import com.carrental.service.RentalService;
import com.carrental.service.UserService;

/**
 * Rental Management Frame for Staff users
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class RentalManagementFrame extends JFrame {

  private RentalService rentalService;
  private CarService carService;
  private CustomerService customerService;
  private AuthenticationService authService;
  private UserService userService;

  private JTable rentalTable;
  private DefaultTableModel tableModel;
  private JComboBox<String> statusFilter;

  // Callback for refreshing parent dashboard
  private Runnable refreshCallback;

  public RentalManagementFrame() {
    this.rentalService = new RentalService();
    this.carService = new CarService();
    this.customerService = new CustomerService();
    this.authService = new AuthenticationService();
    this.userService = new UserService();
    initializeComponents();
    setupLayout();
    setupEventHandlers();
    loadRentals();
    setFrameProperties();
  }

  /**
   * Set callback for refreshing parent dashboard
   */
  public void setRefreshCallback(Runnable callback) {
    this.refreshCallback = callback;
  }

  private void initializeComponents() {
    // Initialize table model
    String[] columnNames = { "ID", "Car", "Customer", "Start Date", "End Date", "Total Cost", "Status", "Staff" };
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Make table read-only
      }
    };

    rentalTable = new JTable(tableModel);
    rentalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    rentalTable.getTableHeader().setReorderingAllowed(false);

    // Apply modern styling to table
    rentalTable.setFont(ModernUITheme.FONT_PRIMARY);
    rentalTable.setRowHeight(30);
    rentalTable.setGridColor(ModernUITheme.BORDER_LIGHT);
    rentalTable.getTableHeader().setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    rentalTable.getTableHeader().setBackground(ModernUITheme.SECONDARY_COLOR);
    rentalTable.getTableHeader().setForeground(ModernUITheme.TEXT_LIGHT);

    // Initialize filter component
    statusFilter = new JComboBox<>(new String[] { "All", "Active", "Completed", "Cancelled" });

    // Apply modern styling
    ModernUITheme.styleComponent(statusFilter);
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
        ModernUITheme.SECONDARY_COLOR,
        ModernUITheme.SUCCESS_COLOR);
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

    JLabel titleLabel = new JLabel("📋 Rental Management");
    titleLabel.setForeground(ModernUITheme.TEXT_LIGHT);
    titleLabel.setFont(ModernUITheme.FONT_HEADING);
    headerPanel.add(titleLabel, BorderLayout.WEST);

    backgroundPanel.add(headerPanel, BorderLayout.NORTH);

    // Filter panel
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    filterPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    filterPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JLabel statusLabel = new JLabel("Status Filter:");
    statusLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    statusLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
    filterPanel.add(statusLabel);
    filterPanel.add(statusFilter);

    JButton refreshButton = ModernUITheme.createModernButton("🔄 Refresh", "primary");
    filterPanel.add(refreshButton);

    // Table panel
    JScrollPane scrollPane = new JScrollPane(rentalTable);
    scrollPane.setPreferredSize(new Dimension(800, 400));
    scrollPane.setBorder(BorderFactory.createLineBorder(ModernUITheme.BORDER_LIGHT, 1));

    // Main content panel
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    mainPanel.add(filterPanel, BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    backgroundPanel.add(mainPanel, BorderLayout.CENTER);

    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
    buttonPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JButton newRentalButton = ModernUITheme.createModernButton("➕ New Rental", "success");
    JButton completeRentalButton = ModernUITheme.createModernButton("✅ Complete Rental", "success");
    JButton viewDetailsButton = ModernUITheme.createModernButton("👁️ View Details", "primary");
    JButton backButton = ModernUITheme.createModernButton("⬅️ Back to Dashboard", "danger");

    buttonPanel.add(newRentalButton);
    buttonPanel.add(completeRentalButton);
    buttonPanel.add(viewDetailsButton);
    buttonPanel.add(backButton);

    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    add(backgroundPanel, BorderLayout.CENTER);

    // Add event handlers
    refreshButton.addActionListener(e -> loadRentals());
    newRentalButton.addActionListener(e -> createNewRental());
    completeRentalButton.addActionListener(e -> completeSelectedRental());
    viewDetailsButton.addActionListener(e -> viewRentalDetails());
    backButton.addActionListener(e -> dispose());
  }

  private void setupEventHandlers() {
    // Status filter change
    statusFilter.addActionListener(e -> loadRentals());

    // Table double-click to view details
    rentalTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          viewRentalDetails();
        }
      }
    });
  }

  private void setFrameProperties() {
    setTitle("Rental Management - Car Rental System");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);
    setResizable(true);
  }

  private void loadRentals() {
    try {
      List<Rental> rentals;
      String statusFilterValue = (String) statusFilter.getSelectedItem();

      if ("All".equals(statusFilterValue)) {
        rentals = rentalService.getAllRentals();
      } else if ("Active".equals(statusFilterValue)) {
        rentals = rentalService.getActiveRentals();
      } else {
        rentals = rentalService.getRentalsByStatus(statusFilterValue.toUpperCase());
      }

      updateTableData(rentals);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading rentals: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void updateTableData(List<Rental> rentals) {
    tableModel.setRowCount(0);
    for (Rental rental : rentals) {
      // Load related objects separately to avoid statement pointer issues
      String carInfo;
      String customerInfo;
      String staffInfo;

      try {
        Car car = carService.getCarById(rental.getCarId());
        carInfo = car != null
            ? car.getMake() + " " + car.getModel() + " (" + car.getLicensePlate() + ")"
            : "Car ID: " + rental.getCarId();
      } catch (Exception e) {
        carInfo = "Car ID: " + rental.getCarId();
      }

      try {
        Customer customer = customerService.getCustomerById(rental.getCustomerId());
        customerInfo = customer != null
            ? customer.getName() + " (" + customer.getEmail() + ")"
            : "Customer ID: " + rental.getCustomerId();
      } catch (Exception e) {
        customerInfo = "Customer ID: " + rental.getCustomerId();
      }

      try {
        User staff = userService.getUserById(rental.getStaffId());
        staffInfo = staff != null ? staff.getUsername() : "Staff ID: " + rental.getStaffId();
      } catch (Exception e) {
        staffInfo = "Staff ID: " + rental.getStaffId();
      }

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
  }

  private void createNewRental() {
    NewRentalDialog dialog = new NewRentalDialog(this);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      loadRentals(); // Refresh table
      // Refresh parent dashboard statistics
      if (refreshCallback != null) {
        refreshCallback.run();
      }
    }
  }

  private void completeSelectedRental() {
    int selectedRow = rentalTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a rental to complete.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int rentalId = (Integer) tableModel.getValueAt(selectedRow, 0);
    String rentalInfo = tableModel.getValueAt(selectedRow, 1) + " - " +
        tableModel.getValueAt(selectedRow, 2);

    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to complete this rental?\n" + rentalInfo,
        "Confirm Complete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        boolean completed = rentalService.completeRental(rentalId);
        if (completed) {
          JOptionPane.showMessageDialog(this,
              "Rental completed successfully.",
              "Success",
              JOptionPane.INFORMATION_MESSAGE);
          loadRentals(); // Refresh table
          // Refresh parent dashboard statistics
          if (refreshCallback != null) {
            refreshCallback.run();
          }
        } else {
          JOptionPane.showMessageDialog(this,
              "Failed to complete rental.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error completing rental: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void viewRentalDetails() {
    int selectedRow = rentalTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a rental to view details.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int rentalId = (Integer) tableModel.getValueAt(selectedRow, 0);
    try {
      Rental rental = rentalService.getRentalById(rentalId);
      if (rental != null) {
        showRentalDetails(rental);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading rental details: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void showRentalDetails(Rental rental) {
    StringBuilder details = new StringBuilder();
    details.append("Rental Details:\n\n");
    details.append("ID: ").append(rental.getId()).append("\n");
    details.append("Car: ").append(rental.getCar() != null
        ? rental.getCar().getMake() + " " + rental.getCar().getModel() + " (" + rental.getCar().getLicensePlate() + ")"
        : "Car ID: " + rental.getCarId()).append("\n");
    details.append("Customer: ")
        .append(
            rental.getCustomer() != null ? rental.getCustomer().getName() + " (" + rental.getCustomer().getEmail() + ")"
                : "Customer ID: " + rental.getCustomerId())
        .append("\n");
    details.append("Start Date: ").append(rental.getStartDate()).append("\n");
    details.append("End Date: ").append(rental.getEndDate()).append("\n");
    details.append("Number of Days: ").append(rental.getNumberOfDays()).append("\n");
    details.append("Total Cost: $").append(rental.getTotalCost()).append("\n");
    details.append("Status: ").append(rental.getStatus()).append("\n");
    details.append("Staff: ")
        .append(rental.getStaff() != null ? rental.getStaff().getUsername() : "Staff ID: " + rental.getStaffId())
        .append("\n");
    details.append("Created: ").append(rental.getCreatedDate() != null ? rental.getCreatedDate().toString() : "N/A");

    JOptionPane.showMessageDialog(this,
        details.toString(),
        "Rental Details",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Inner class for new rental dialog
   */
  private class NewRentalDialog extends JDialog {
    private boolean confirmed = false;

    private JComboBox<Car> carCombo;
    private JComboBox<Customer> customerCombo;
    private JSpinner startDateSpinner, endDateSpinner;
    private JLabel totalCostLabel;

    public NewRentalDialog(JFrame parent) {
      super(parent, "Create New Rental", true);
      initializeDialog();
    }

    private void initializeDialog() {
      setLayout(new BorderLayout());

      // Form panel with better spacing
      JPanel formPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(8, 10, 8, 10);
      gbc.anchor = GridBagConstraints.WEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;

      try {
        // Initialize components
        List<Car> availableCars = carService.getAvailableCars();
        List<Customer> customers = customerService.getAllCustomers();

        carCombo = new JComboBox<>(availableCars.toArray(new Car[0]));
        customerCombo = new JComboBox<>(customers.toArray(new Customer[0]));

        // Create date spinners with calendar functionality
        java.util.Date today = java.sql.Date.valueOf(LocalDate.now());
        java.util.Date tomorrow = java.sql.Date.valueOf(LocalDate.now().plusDays(1));

        startDateSpinner = new JSpinner(new SpinnerDateModel(today, null, null, java.util.Calendar.DAY_OF_MONTH));
        endDateSpinner = new JSpinner(new SpinnerDateModel(tomorrow, null, null, java.util.Calendar.DAY_OF_MONTH));

        // Set spinner editor to show only date
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startEditor);
        endDateSpinner.setEditor(endEditor);

        totalCostLabel = new JLabel("$0.00");

        // Apply modern styling
        ModernUITheme.styleComponent(carCombo);
        ModernUITheme.styleComponent(customerCombo);
        ModernUITheme.styleComponent(startDateSpinner);
        ModernUITheme.styleComponent(endDateSpinner);
        totalCostLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        totalCostLabel.setForeground(ModernUITheme.SUCCESS_COLOR);

        // Add components to form with better layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel carLabel = new JLabel("Car:");
        carLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        formPanel.add(carLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        formPanel.add(carCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel customerLabel = new JLabel("Customer:");
        customerLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        formPanel.add(customerLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        formPanel.add(customerCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel startDateLabel = new JLabel("Start Date:");
        startDateLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        formPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        formPanel.add(startDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel endDateLabel = new JLabel("End Date:");
        endDateLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        formPanel.add(endDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        formPanel.add(endDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel costLabel = new JLabel("Total Cost:");
        costLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        formPanel.add(costLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.7;
        formPanel.add(totalCostLabel, gbc);

        // Add listeners for cost calculation
        carCombo.addActionListener(e -> calculateTotalCost());
        startDateSpinner.addChangeListener(e -> calculateTotalCost());
        endDateSpinner.addChangeListener(e -> calculateTotalCost());

        // Calculate initial cost
        calculateTotalCost();

      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error loading data: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        dispose();
        return;
      }

      add(formPanel, BorderLayout.CENTER);

      // Button panel
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
      JButton createButton = ModernUITheme.createModernButton("Create Rental", "success");
      JButton cancelButton = ModernUITheme.createModernButton("Cancel", "danger");

      createButton.addActionListener(e -> createRental());
      cancelButton.addActionListener(e -> dispose());

      buttonPanel.add(createButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);

      setSize(500, 400);
      setLocationRelativeTo(getParent());
      setResizable(true);
      setMinimumSize(new Dimension(450, 350));
    }

    private void calculateTotalCost() {
      try {
        Car selectedCar = (Car) carCombo.getSelectedItem();
        if (selectedCar == null) {
          totalCostLabel.setText("$0.00");
          return;
        }

        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();

        LocalDate startLocalDate = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        long numberOfDays = java.time.temporal.ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;
        BigDecimal totalCost = selectedCar.getDailyRate().multiply(BigDecimal.valueOf(numberOfDays));

        totalCostLabel.setText("$" + totalCost);
      } catch (Exception e) {
        totalCostLabel.setText("$0.00");
      }
    }

    private void createRental() {
      try {
        Car selectedCar = (Car) carCombo.getSelectedItem();
        Customer selectedCustomer = (Customer) customerCombo.getSelectedItem();

        if (selectedCar == null || selectedCustomer == null) {
          JOptionPane.showMessageDialog(this,
              "Please select both a car and a customer.",
              "Invalid Selection",
              JOptionPane.WARNING_MESSAGE);
          return;
        }

        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();

        LocalDate startLocalDate = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        // Get current staff ID - handle null user gracefully
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
          JOptionPane.showMessageDialog(this,
              "Authentication error: Please log in again.",
              "Authentication Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        int staffId = currentUser.getId();

        boolean created = rentalService.createRental(
            selectedCar.getId(),
            selectedCustomer.getId(),
            staffId,
            startLocalDate,
            endLocalDate);

        if (created) {
          confirmed = true;
          dispose();
        } else {
          JOptionPane.showMessageDialog(this,
              "Failed to create rental.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }

      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error creating rental: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }

    public boolean isConfirmed() {
      return confirmed;
    }
  }
}
