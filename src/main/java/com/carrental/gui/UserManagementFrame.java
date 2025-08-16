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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.codec.digest.DigestUtils;

import com.carrental.model.User;
import com.carrental.service.UserService;

/**
 * User Management Interface for Admin users
 * Allows admin to manage staff user accounts
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class UserManagementFrame extends JFrame {

  private UserService userService;
  private JTable userTable;
  private DefaultTableModel tableModel;
  private JTextField searchField;

  public UserManagementFrame() {
    this.userService = new UserService();
    initializeComponents();
    setupLayout();
    setupEventHandlers();
    loadUsers();
    setFrameProperties();
  }

  private void initializeComponents() {
    // Initialize table model
    String[] columnNames = { "ID", "Username", "Role", "Created Date" };
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Make table read-only
      }
    };

    userTable = new JTable(tableModel);
    userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    userTable.getTableHeader().setReorderingAllowed(false);

    // Apply modern styling to table
    userTable.setFont(ModernUITheme.FONT_PRIMARY);
    userTable.setRowHeight(30);
    userTable.setGridColor(ModernUITheme.BORDER_LIGHT);
    userTable.getTableHeader().setFont(ModernUITheme.FONT_PRIMARY_BOLD);
    userTable.getTableHeader().setBackground(ModernUITheme.PRIMARY_COLOR);
    userTable.getTableHeader().setForeground(ModernUITheme.TEXT_LIGHT);

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
        ModernUITheme.PRIMARY_COLOR,
        ModernUITheme.PRIMARY_DARK);
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

    JLabel titleLabel = new JLabel("👥 User Management");
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
    JScrollPane scrollPane = new JScrollPane(userTable);
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

    JButton addButton = ModernUITheme.createModernButton("➕ Add User", "success");
    JButton editButton = ModernUITheme.createModernButton("✏️ Edit User", "primary");
    JButton deleteButton = ModernUITheme.createModernButton("🗑️ Delete User", "danger");
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
      loadUsers();
    });
    addButton.addActionListener(e -> addNewUser());
    editButton.addActionListener(e -> editSelectedUser());
    deleteButton.addActionListener(e -> deleteSelectedUser());
    refreshButton.addActionListener(e -> loadUsers());
    backButton.addActionListener(e -> dispose());
  }

  private void setupEventHandlers() {
    // Search field enter key
    searchField.addActionListener(e -> performSearch());

    // Table double-click to edit
    userTable.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          editSelectedUser();
        }
      }
    });
  }

  private void setFrameProperties() {
    setTitle("User Management - Car Rental System");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);
    setResizable(true);
  }

  private void loadUsers() {
    try {
      List<User> users = userService.getAllUsers();
      updateTableData(users);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading users: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void updateTableData(List<User> users) {
    tableModel.setRowCount(0);
    for (User user : users) {
      Object[] row = {
          user.getId(),
          user.getUsername(),
          user.getRole(),
          user.getCreatedDate() != null ? user.getCreatedDate().toString() : ""
      };
      tableModel.addRow(row);
    }
  }

  private void performSearch() {
    try {
      String searchTerm = searchField.getText().trim();
      List<User> users;

      if (searchTerm.isEmpty()) {
        users = userService.getAllUsers();
      } else {
        users = userService.searchUsers(searchTerm);
      }

      updateTableData(users);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error searching users: " + e.getMessage(),
          "Search Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void addNewUser() {
    UserDialog dialog = new UserDialog(this, null);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      loadUsers(); // Refresh table
    }
  }

  private void editSelectedUser() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a user to edit.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
    try {
      User user = userService.getUserById(userId);
      if (user != null) {
        UserDialog dialog = new UserDialog(this, user);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
          loadUsers(); // Refresh table
        }
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Error loading user details: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelectedUser() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Please select a user to delete.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
    String username = (String) tableModel.getValueAt(selectedRow, 1);
    String role = (String) tableModel.getValueAt(selectedRow, 2);

    // Prevent deletion of admin user
    if ("ADMIN".equals(role)) {
      JOptionPane.showMessageDialog(this,
          "Cannot delete admin user.",
          "Delete Restricted",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete user '" + username + "'?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
          JOptionPane.showMessageDialog(this,
              "User deleted successfully.",
              "Success",
              JOptionPane.INFORMATION_MESSAGE);
          loadUsers(); // Refresh table
        } else {
          JOptionPane.showMessageDialog(this,
              "Failed to delete user.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error deleting user: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Inner class for user dialog (add/edit)
   */
  private class UserDialog extends JDialog {
    private User user;
    private boolean confirmed = false;

    private JTextField usernameField, passwordField;
    private JComboBox<String> roleCombo;

    public UserDialog(JFrame parent, User user) {
      super(parent, user == null ? "Add New User" : "Edit User", true);
      this.user = user;
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
      usernameField = new JTextField(20);
      passwordField = new JPasswordField(20);
      roleCombo = new JComboBox<>(new String[] { "STAFF", "ADMIN" });

      // Add components to form
      gbc.gridx = 0;
      gbc.gridy = 0;
      formPanel.add(new JLabel("Username:"), gbc);
      gbc.gridx = 1;
      formPanel.add(usernameField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 1;
      formPanel.add(new JLabel("Password:"), gbc);
      gbc.gridx = 1;
      formPanel.add(passwordField, gbc);

      gbc.gridx = 0;
      gbc.gridy = 2;
      formPanel.add(new JLabel("Role:"), gbc);
      gbc.gridx = 1;
      formPanel.add(roleCombo, gbc);

      add(formPanel, BorderLayout.CENTER);

      // Button panel
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
      JButton saveButton = ModernUITheme.createModernButton("Save", "success");
      JButton cancelButton = ModernUITheme.createModernButton("Cancel", "danger");

      saveButton.addActionListener(e -> saveUser());
      cancelButton.addActionListener(e -> dispose());

      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);

      // Load user data if editing
      if (user != null) {
        loadUserData();
      }

      setSize(400, 200);
      setLocationRelativeTo(getParent());
    }

    private void loadUserData() {
      usernameField.setText(user.getUsername());
      usernameField.setEnabled(false); // Don't allow username editing
      roleCombo.setSelectedItem(user.getRole());
    }

    private void saveUser() {
      try {
        String username = usernameField.getText().trim();
        String password = new String(((JPasswordField) passwordField).getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (user == null) {
          // Add new user
          userService.createUser(username, password, role);
        } else {
          // Update existing user
          if (!password.isEmpty()) {
            user.setPassword(DigestUtils.sha256Hex(password));
          }
          user.setRole(role);
          userService.updateUser(user);
        }

        confirmed = true;
        dispose();

      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error saving user: " + e.getMessage(),
            "Save Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }

    public boolean isConfirmed() {
      return confirmed;
    }
  }
}
