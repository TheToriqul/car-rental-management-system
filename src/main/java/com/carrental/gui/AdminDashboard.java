package com.carrental.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Admin Dashboard for managing the car rental system
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class AdminDashboard extends JFrame {
    
    public AdminDashboard() {
        initializeComponents();
        setupLayout();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        // TODO: Implement admin dashboard components
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(51, 122, 183));
        JLabel titleLabel = new JLabel("Admin Dashboard - Car Rental Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add placeholder buttons
        JButton userManagementBtn = new JButton("User Management");
        JButton carManagementBtn = new JButton("Car Management");
        JButton reportsBtn = new JButton("Reports");
        JButton logoutBtn = new JButton("Logout");
        
        mainPanel.add(userManagementBtn);
        mainPanel.add(carManagementBtn);
        mainPanel.add(reportsBtn);
        mainPanel.add(logoutBtn);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setFrameProperties() {
        setTitle("Admin Dashboard - Car Rental Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
}
