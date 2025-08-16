package com.carrental.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import com.carrental.model.Customer;
import com.carrental.service.CustomerService;

/**
 * Customer Management Frame for Staff users
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class CustomerManagementFrame extends JFrame {

  private CustomerService customerService;
  private JTable customerTable;
  private DefaultTableModel tableModel;
  private JTextField searchField;

  public CustomerManagementFrame() {
    this.customerService = new CustomerService();
    initializeComponents();
    setupLayout();
    setupEventHandlers();
    loadCustomers();
    setFrameProperties();
  }

  private void initializeComponents() {
    // Initialize table model
    String[] columnNames = { "ID", "Name", "Email", "Phone", "Address", "Created Date" };
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Make table read-only
      }
    };

    customerTable = new JTable(tableModel);
    customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    customerTable.getTableHeader().setReorderingAllowed(false);

    // Apply modern styling to table
    customerTable.setFont(ModernUITheme.FONT_PRIMARY);
    customerTable.setRowHeight(30);
    customerTable.setGridColor(ModernUITheme.BORDER_LIGHT);
    customerTable.getTableHeader().setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    customerTable.getTableHeader().setBackground(ModernUITheme.SECONDARY_COLOR);
    customerTable.getTableHeader().setForeground(ModernUITheme.TEXT_LIGHT);

    // Initialize search component
    searchField = new JTextField(20);

    // Apply modern styling
    ModernUITheme.styleComponent(searchField);
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
        ModernUITheme.SECONDARY_COLOR,
        ModernUITheme.SUCCESS_COLOR);
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

    JLabel titleLabel = new JLabel("👤 Customer Management");
    titleLabel.setForeground(ModernUITheme.TEXT_LIGHT);
    titleLabel.setFont(ModernUITheme.FONT_HEADING);
    headerPanel.add(titleLabel, BorderLayout.WEST);

    backgroundPanel.add(headerPanel, BorderLayout.NORTH);

    // Search panel
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    searchPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
    searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JLabel searchLabel = new JLabel("Search:");
    searchLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    searchLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
    searchPanel.add(searchLabel);
    searchPanel.add(searchField);

    JButton searchButton = ModernUITheme.createModernButton("🔍 Search", "primary");
    JButton clearButton = ModernUITheme.createModernButton("🗑️ Clear", "danger");
    searchPanel.add(searchButton);
    searchPanel.add(clearButton);

    // Table panel
    JScrollPane scrollPane = new JScrollPane(customerTable);
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

    JButton addButton = ModernUITheme.createModernButton("➕ Add Customer", "success");
    JButton editButton = ModernUITheme.createModernButton("✏️ Edit Customer", "primary");
    JButton deleteButton = ModernUITheme.createModernButton("🗑️ Delete Customer", "danger");
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
      loadCustomers();
    });
    addButton.addActionListener(e -> addNewCustomer());
    editButton.addActionListener(e -> editSelectedCustomer());
    deleteButton.addActionListener(e -> deleteSelectedCustomer());
    refreshButton.addActionListener(e -> loadCustomers());
    backButton.addActionListener(e -> dispose());
  }

  private void setupEventHandlers() {
    // Search field enter key
    searchField.addActionListener(e -> performSearch());

    // Table double-click to edit
    customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          editSelectedCustomer();
        }
      }
    });
  }

  private void setFrameProperties() {
    setTitle("Customer Management - Car Rental System");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);
    setResizable(true);
  }

  private void loadCustomers() {
    try {
      List<Customer> customers = customerService.getAllCustomers();
      updateTableData(customers);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading customers: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void updateTableData(List<Customer> customers) {
    tableModel.setRowCount(0);
    for (Customer customer : customers) {
      Object[] row = {
          customer.getId(),
          customer.getName(),
          customer.getEmail(),
          customer.getPhone(),
          customer.getAddress(),
          customer.getCreatedDate() != null ? customer.getCreatedDate().toString() : ""
      };
      tableModel.addRow(row);
    }
  }

  private void performSearch() {
    try {
      String searchTerm = searchField.getText().trim();
      List<Customer> customers;

      if (searchTerm.isEmpty()) {
        customers = customerService.getAllCustomers();
      } else {
        customers = customerService.searchCustomers(searchTerm);
      }

      updateTableData(customers);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error searching customers: " + e.getMessage(),
          "Search Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void addNewCustomer() {
    CustomerDialog dialog = new CustomerDialog(this, null);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      loadCustomers(); // Refresh table
    }
  }

  private void editSelectedCustomer() {
    int selectedRow = customerTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a customer to edit.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int customerId = (Integer) tableModel.getValueAt(selectedRow, 0);
    try {
      Customer customer = customerService.getCustomerById(customerId);
      if (customer != null) {
        CustomerDialog dialog = new CustomerDialog(this, customer);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
          loadCustomers(); // Refresh table
        }
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading customer details: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelectedCustomer() {
    int selectedRow = customerTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a customer to delete.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int customerId = (Integer) tableModel.getValueAt(selectedRow, 0);
    String customerInfo = tableModel.getValueAt(selectedRow, 1) + " (" +
        tableModel.getValueAt(selectedRow, 2) + ")";

    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete this customer?\n" + customerInfo,
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        boolean deleted = customerService.deleteCustomer(customerId);
        if (deleted) {
          JOptionPane.showMessageDialog(this,
              "Customer deleted successfully.",
              "Success",
              JOptionPane.INFORMATION_MESSAGE);
          loadCustomers(); // Refresh table
        } else {
          JOptionPane.showMessageDialog(this,
              "Failed to delete customer.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error deleting customer: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Inner class for customer dialog (add/edit)
   */
  private class CustomerDialog extends JDialog {
    private Customer customer;
    private boolean confirmed = false;

    private JTextField nameField, emailField, phoneField, addressField;

    public CustomerDialog(JFrame parent, Customer customer) {
      super(parent, customer == null ? "Add New Customer" : "Edit Customer", true);
      this.customer = customer;
      initializeDialog();
    }

    private void initializeDialog() {
      setLayout(new BorderLayout());

      // Form panel
      JPanel formPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.anchor = GridBagConstraints.WEST;

      // Initialize fields
      nameField = new JTextField(20);
      emailField = new JTextField(20);
      phoneField = new JTextField(20);
      addressField = new JTextField(20);

      // Add components to form
      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(new JLabel("Name:"), gbc);
      gbc.gridx = 1;
      formPanel.add(nameField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 1;
      formPanel.add(new JLabel("Email:"), gbc);
      gbc.gridx = 1;
      formPanel.add(emailField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 2;
      formPanel.add(new JLabel("Phone:"), gbc);
      gbc.gridx = 1;
      formPanel.add(phoneField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 3;
      formPanel.add(new JLabel("Address:"), gbc);
      gbc.gridx = 1;
      formPanel.add(addressField, gbc);

      add(formPanel, BorderLayout.CENTER);

      // Button panel
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
      JButton saveButton = ModernUITheme.createModernButton("Save", "success");
      JButton cancelButton = ModernUITheme.createModernButton("Cancel", "danger");

      saveButton.addActionListener(e -> saveCustomer());
      cancelButton.addActionListener(e -> dispose());

      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);

      // Load customer data if editing
      if (customer != null) {
        loadCustomerData();
      }

      setSize(400, 250);
      setLocationRelativeTo(getParent());
    }

    private void loadCustomerData() {
      nameField.setText(customer.getName());
      emailField.setText(customer.getEmail());
      phoneField.setText(customer.getPhone());
      addressField.setText(customer.getAddress());
    }

    private void saveCustomer() {
      try {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (customer == null) {
          // Add new customer
          customerService.createCustomer(name, email, phone, address);
        } else {
          // Update existing customer
          customer.setName(name);
          customer.setEmail(email);
          customer.setPhone(phone);
          customer.setAddress(address);
          customerService.updateCustomer(customer);
        }

        confirmed = true;
        dispose();

      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error saving customer: " + e.getMessage(),
            "Save Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }

    public boolean isConfirmed() {
      return confirmed;
    }
  }
}
