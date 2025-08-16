package com.carrental.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Staff Dashboard for managing car rentals and customers
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class StaffDashboard extends JFrame {
    
    public StaffDashboard() {
        initializeComponents();
        setupLayout();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        // TODO: Implement staff dashboard components
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(92, 184, 92));
        JLabel titleLabel = new JLabel("Staff Dashboard - Car Rental Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add placeholder buttons
        JButton rentalManagementBtn = new JButton("Rental Management");
        JButton customerManagementBtn = new JButton("Customer Management");
        JButton searchBtn = new JButton("Search Cars");
        JButton logoutBtn = new JButton("Logout");
        
        mainPanel.add(rentalManagementBtn);
        mainPanel.add(customerManagementBtn);
        mainPanel.add(searchBtn);
        mainPanel.add(logoutBtn);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setFrameProperties() {
        setTitle("Staff Dashboard - Car Rental Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
}
