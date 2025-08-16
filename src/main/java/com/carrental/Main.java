package com.carrental;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.carrental.gui.LoginFrame;
import com.carrental.util.DatabaseManager;

/**
 * Main entry point for the Car Rental Management System
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class Main {

  public static void main(String[] args) {
    // Set system look and feel
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
    } catch (Exception e) {
      System.err.println("Error setting look and feel: " + e.getMessage());
    }

    // Initialize database
    try {
      DatabaseManager.initializeDatabase();
      System.out.println("Database initialized successfully");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null,
          "Error initializing database: " + e.getMessage(),
          "Database Error",
          JOptionPane.ERROR_MESSAGE);
      System.err.println("Database initialization failed: " + e.getMessage());
      System.exit(1);
    }

    // Launch application on Event Dispatch Thread
    SwingUtilities.invokeLater(() -> {
      try {
        // Create and display login frame
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);

        // Center the frame on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = loginFrame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        loginFrame.setLocation(x, y);

        System.out.println("Car Rental Management System started successfully");
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Error starting application: " + e.getMessage(),
            "Application Error",
            JOptionPane.ERROR_MESSAGE);
        System.err.println("Application startup failed: " + e.getMessage());
        System.exit(1);
      }
    });
  }
}
