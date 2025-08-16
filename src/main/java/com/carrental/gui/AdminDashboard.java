package com.carrental.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

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
 * Admin Dashboard for managing the car rental system
 * 
 * @author Md Toriqul Islam
 * @version 1.0.createStatCard
 */
public class AdminDashboard extends JFrame {

    private CarService carService;
    private CustomerService customerService;
    private RentalService rentalService;
    private UserService userService;
    private AuthenticationService authService;

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JPanel dashboardPanel;
    private JPanel userManagementPanel;
    private JPanel carManagementPanel;
    private JPanel customerManagementPanel;
    private JPanel rentalManagementPanel;
    private JPanel reportsPanel;

    // Dynamic header label
    private JLabel welcomeLabel;

    public AdminDashboard() {
        this.carService = new CarService();
        this.customerService = new CustomerService();
        this.rentalService = new RentalService();
        this.userService = new UserService();
        this.authService = new AuthenticationService();

        initializeComponents();
        setupLayout();
        setFrameProperties();
    }

    private void initializeComponents() {
        // Initialize card layout for switching between panels
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setOpaque(false);

        // Initialize all panels
        dashboardPanel = createDashboardPanel();
        userManagementPanel = createUserManagementPanel();
        carManagementPanel = createCarManagementPanel();
        customerManagementPanel = createCustomerManagementPanel();
        rentalManagementPanel = createRentalManagementPanel();
        reportsPanel = createReportsPanel();

        // Add panels to card layout
        mainContentPanel.add(dashboardPanel, "DASHBOARD");
        mainContentPanel.add(userManagementPanel, "USER_MANAGEMENT");
        mainContentPanel.add(carManagementPanel, "CAR_MANAGEMENT");
        mainContentPanel.add(customerManagementPanel, "CUSTOMER_MANAGEMENT");
        mainContentPanel.add(rentalManagementPanel, "RENTAL_MANAGEMENT");
        mainContentPanel.add(reportsPanel, "REPORTS");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create beautiful gradient background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Create beautiful gradient from light blue to white
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(240, 248, 255),
                        getWidth(), getHeight(), new Color(255, 255, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add subtle pattern overlay
                g2d.setColor(new Color(52, 152, 219, 5));
                for (int i = 0; i < getWidth(); i += 40) {
                    for (int j = 0; j < getHeight(); j += 40) {
                        g2d.fillOval(i, j, 3, 3);
                    }
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Enhanced header panel with gradient and shadow - rounded bottom corners only
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow with rounded bottom corners only
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 0, 20);

                // Draw gradient background with rounded bottom corners only
                GradientPaint gradient = new GradientPaint(
                        0, 0, ModernUITheme.PRIMARY_COLOR,
                        0, getHeight(), ModernUITheme.PRIMARY_DARK);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 0, 20);
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(20, 30, 25, 30)); // Increased bottom padding for rounded corners
        headerPanel.setOpaque(false);

        // Enhanced welcome message with icon - will be updated dynamically
        welcomeLabel = new JLabel(getAdminWelcomeMessage());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(ModernUITheme.TEXT_LIGHT);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // Navigation buttons
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

        JButton homeBtn = ModernUITheme.createModernButton("🏠 Home", "primary");
        homeBtn.setPreferredSize(new Dimension(100, 35));
        homeBtn.addActionListener(e -> showDashboard());

        JButton logoutBtn = ModernUITheme.createModernButton("🚪 Logout", "danger");
        logoutBtn.setPreferredSize(new Dimension(100, 35));

        navPanel.add(homeBtn);
        navPanel.add(logoutBtn);
        headerPanel.add(navPanel, BorderLayout.EAST);

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);

        // Show dashboard by default
        showDashboard();

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Statistics panel at top
        JPanel statsPanel = createStatisticsPanel();
        panel.add(statsPanel, BorderLayout.NORTH);

        // Main content area with responsive 2-column layout
        JPanel mainContentArea = new JPanel(new BorderLayout());
        mainContentArea.setOpaque(false);
        mainContentArea.setBorder(new EmptyBorder(0, 40, 50, 50)); // Adjusted padding for responsive layout

        // Left Sidebar - Navigation with visual separation
        JPanel leftSidebar = createLeftSidebar();
        leftSidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 15, 20, 15) // Adjusted padding for responsive layout
        ));
        mainContentArea.add(leftSidebar, BorderLayout.WEST);

        // Main Content Area - Welcome and Overview with visual separation
        JPanel mainContent = createMainContent();
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Adjusted padding

        // Make main content scrollable
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Style the scrollbar
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(52, 152, 219);
                this.trackColor = new Color(240, 240, 240);
            }
        });

        mainContentArea.add(scrollPane, BorderLayout.CENTER);

        panel.add(mainContentArea, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLeftSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(280, 0)); // Increased from 240px for better text fit
        sidebar.setMinimumSize(new Dimension(250, 0)); // Minimum width
        sidebar.setMaximumSize(new Dimension(350, Integer.MAX_VALUE)); // Maximum width
        sidebar.setOpaque(false);

        // Sidebar Header with professional styling
        JPanel headerPanel = createSidebarHeader();
        sidebar.add(headerPanel, BorderLayout.NORTH);

        // Navigation menu with professional styling
        JPanel menuPanel = createNavigationMenu();
        sidebar.add(menuPanel, BorderLayout.CENTER);

        return sidebar;
    }

    private JPanel createSidebarHeader() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw beautiful gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(75, 0, 130), // Deep purple
                        0, getHeight(), new Color(138, 43, 226)); // Blue violet
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw subtle shadow
                g2d.setColor(new Color(0, 0, 0, 25));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);

                // Add subtle pattern overlay
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < getWidth(); i += 20) {
                    for (int j = 0; j < getHeight(); j += 20) {
                        g2d.fillOval(i, j, 2, 2);
                    }
                }
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        headerPanel.setOpaque(false);

        JLabel sidebarHeader = new JLabel("🚀 Navigation Menu");
        sidebarHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sidebarHeader.setForeground(Color.WHITE);
        headerPanel.add(sidebarHeader, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createNavigationMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 0, 12)); // Reduced spacing for better fit
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Navigation Buttons with attractive styling
        JButton userManagementBtn = createProfessionalMenuButton("👥 User Management",
                "Manage system users and permissions", new Color(255, 105, 180)); // Hot pink
        JButton carManagementBtn = createProfessionalMenuButton("🚗 Car Management",
                "Manage fleet and vehicle information", new Color(0, 255, 127)); // Spring green
        JButton customerManagementBtn = createProfessionalMenuButton("👤 Customer Management",
                "Manage customer information", new Color(255, 165, 0)); // Orange
        JButton rentalManagementBtn = createProfessionalMenuButton("📋 Rental Management",
                "Manage car rentals and bookings", new Color(147, 112, 219)); // Medium purple
        JButton reportsBtn = createProfessionalMenuButton("📊 Reports & Analytics",
                "View system reports and statistics", new Color(64, 224, 208)); // Turquoise

        // Add event handlers
        userManagementBtn.addActionListener(e -> showUserManagement());
        carManagementBtn.addActionListener(e -> showCarManagement());
        customerManagementBtn.addActionListener(e -> showCustomerManagement());
        rentalManagementBtn.addActionListener(e -> showRentalManagement());
        reportsBtn.addActionListener(e -> showReports());

        // Add buttons to menu
        menuPanel.add(userManagementBtn);
        menuPanel.add(carManagementBtn);
        menuPanel.add(customerManagementBtn);
        menuPanel.add(rentalManagementBtn);
        menuPanel.add(reportsBtn);

        return menuPanel;
    }

    private JButton createProfessionalMenuButton(String title, String description, Color accentColor) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background with attractive styling
                if (getModel().isRollover()) {
                    // Hover state - beautiful gradient background
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25),
                            0, getHeight(),
                            new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                    // Hover border with glow effect
                    g2d.setColor(accentColor);
                    g2d.setStroke(new java.awt.BasicStroke(2.5f));
                    g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);

                    // Subtle glow effect
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                    g2d.setStroke(new java.awt.BasicStroke(1));
                    g2d.drawRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                } else {
                    // Normal state - elegant background
                    GradientPaint normalGradient = new GradientPaint(
                            0, 0, new Color(255, 255, 255),
                            0, getHeight(), new Color(248, 249, 250));
                    g2d.setPaint(normalGradient);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                    // Subtle border
                    g2d.setColor(new Color(222, 226, 230));
                    g2d.setStroke(new java.awt.BasicStroke(1));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }

                // Draw content with professional typography
                g2d.setColor(ModernUITheme.TEXT_PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2d.getFontMetrics();
                int titleX = 15;
                int titleY = 25;
                g2d.drawString(title, titleX, titleY);

                // Draw description with smaller font and better wrapping
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2d.setColor(ModernUITheme.TEXT_SECONDARY);

                // Responsive text wrapping based on button width
                int maxWidth = getWidth() - 30;
                String[] words = description.split(" ");
                StringBuilder line1 = new StringBuilder();
                StringBuilder line2 = new StringBuilder();
                int currentLine = 0;

                for (String word : words) {
                    if (currentLine == 0) {
                        String testLine = line1.length() > 0 ? line1.toString() + " " + word : word;
                        if (fm.stringWidth(testLine) < maxWidth) {
                            if (line1.length() > 0)
                                line1.append(" ");
                            line1.append(word);
                        } else {
                            currentLine = 1;
                            line2.append(word);
                        }
                    } else {
                        if (line2.length() > 0)
                            line2.append(" ");
                        line2.append(word);
                    }
                }

                int descX = 15;
                int descY1 = 40;
                int descY2 = 52;

                g2d.drawString(line1.toString(), descX, descY1);
                if (line2.length() > 0) {
                    g2d.drawString(line2.toString(), descX, descY2);
                }

                // Draw attractive accent line on the left with gradient
                GradientPaint accentGradient = new GradientPaint(
                        0, 0, accentColor,
                        0, getHeight(),
                        new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 180));
                g2d.setPaint(accentGradient);
                g2d.setStroke(new java.awt.BasicStroke(4));
                g2d.drawLine(0, 0, 0, getHeight());

                // Add subtle highlight on top of accent line
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new java.awt.BasicStroke(1));
                g2d.drawLine(1, 0, 1, getHeight());
            }
        };

        // Responsive sizing - no fixed dimensions
        button.setPreferredSize(new Dimension(280, 70));
        button.setMinimumSize(new Dimension(250, 65));
        button.setMaximumSize(new Dimension(350, 80));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        return button;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Main content header
        JPanel contentHeader = createContentHeader();
        mainContent.add(contentHeader, BorderLayout.NORTH);

        // Data overview content
        JPanel dataOverview = createDataOverview();
        mainContent.add(dataOverview, BorderLayout.CENTER);

        return mainContent;
    }

    private JPanel createContentHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JLabel title = new JLabel("📊 Business Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(ModernUITheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Real-time data insights and performance metrics");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(ModernUITheme.TEXT_SECONDARY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.CENTER);

        return header;
    }

    private JPanel createDataOverview() {
        JPanel overview = new JPanel(new BorderLayout());
        overview.setOpaque(false);

        // Top row: Charts and metrics
        JPanel topRow = new JPanel(new GridLayout(1, 2, 20, 0));
        topRow.setOpaque(false);

        // Revenue chart
        JPanel revenueChart = createRevenueChart();
        topRow.add(revenueChart);

        // Fleet utilization chart
        JPanel fleetChart = createFleetUtilizationChart();
        topRow.add(fleetChart);

        overview.add(topRow, BorderLayout.NORTH);

        // Bottom row: Recent transactions and alerts
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomRow.setOpaque(false);
        bottomRow.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Recent transactions
        JPanel recentTransactions = createRecentTransactions();
        bottomRow.add(recentTransactions);

        // System alerts
        JPanel systemAlerts = createSystemAlerts();
        bottomRow.add(systemAlerts);

        overview.add(bottomRow, BorderLayout.CENTER);

        return overview;
    }

    private JPanel createRevenueChart() {
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw chart background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw border
                g2d.setColor(new Color(52, 152, 219));
                g2d.setStroke(new java.awt.BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // Draw chart title
                g2d.setColor(ModernUITheme.TEXT_PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2d.drawString("💰 Revenue Trend", 20, 30);

                // Get real data from database
                List<Rental> rentals = new ArrayList<>();
                try {
                    rentals = rentalService.getAllRentals();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Calculate revenue data (last 6 months or available data)
                int[] data = calculateRevenueData(rentals);
                int barWidth = (getWidth() - 60) / data.length;
                int maxValue = Arrays.stream(data).max().orElse(1000);
                if (maxValue == 0)
                    maxValue = 1000; // Prevent division by zero
                int chartHeight = getHeight() - 80;
                int startY = 60;

                for (int i = 0; i < data.length; i++) {
                    int barHeight = (int) ((double) data[i] / maxValue * chartHeight);
                    int x = 30 + i * barWidth;
                    int y = startY + chartHeight - barHeight;

                    // Draw bar
                    GradientPaint barGradient = new GradientPaint(
                            x, y, new Color(52, 152, 219),
                            x, y + barHeight, new Color(41, 128, 185));
                    g2d.setPaint(barGradient);
                    g2d.fillRoundRect(x, y, barWidth - 10, barHeight, 5, 5);

                    // Draw value
                    g2d.setColor(ModernUITheme.TEXT_PRIMARY);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    String value = "$" + data[i];
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = x + (barWidth - 10 - fm.stringWidth(value)) / 2;
                    g2d.drawString(value, textX, y - 5);
                }
            }
        };

        chart.setPreferredSize(new Dimension(400, 200));
        chart.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chart.setOpaque(false);

        return chart;
    }

    private int[] calculateRevenueData(List<Rental> rentals) {
        // Calculate revenue for last 6 months
        int[] monthlyRevenue = new int[6];
        java.time.LocalDate now = java.time.LocalDate.now();

        for (Rental rental : rentals) {
            if ("COMPLETED".equals(rental.getStatus())) {
                try {
                    // Parse rental date and calculate which month it belongs to
                    java.time.LocalDate rentalDate = rental.getStartDate();
                    if (rentalDate != null) {
                        int monthsAgo = (int) java.time.temporal.ChronoUnit.MONTHS.between(rentalDate, now);

                        if (monthsAgo >= 0 && monthsAgo < 6) {
                            // Calculate revenue using actual total cost
                            int revenue = rental.getTotalCost() != null ? rental.getTotalCost().intValue() : 0;
                            monthlyRevenue[monthsAgo] += revenue;
                        }
                    }
                } catch (Exception e) {
                    // Skip invalid dates
                }
            }
        }

        return monthlyRevenue;
    }

    private JPanel createFleetUtilizationChart() {
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw chart background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw border
                g2d.setColor(new Color(46, 204, 113));
                g2d.setStroke(new java.awt.BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // Draw chart title
                g2d.setColor(ModernUITheme.TEXT_PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2d.drawString("🚗 Fleet Utilization", 20, 30);

                // Get real data from database
                List<Car> cars = new ArrayList<>();
                List<Rental> rentals = new ArrayList<>();
                try {
                    cars = carService.getAllCars();
                    rentals = rentalService.getAllRentals();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Calculate fleet utilization
                int totalCars = cars.size();
                int rentedCars = (int) rentals.stream()
                        .filter(r -> "ACTIVE".equals(r.getStatus()))
                        .count();
                int availableCars = totalCars - rentedCars;

                if (totalCars == 0) {
                    totalCars = 1; // Prevent division by zero
                    availableCars = 1;
                    rentedCars = 0;
                }

                // Calculate percentages
                int availablePercent = (availableCars * 360) / totalCars;
                int rentedPercent = 360 - availablePercent;

                // Draw pie chart
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2 + 20;
                int radius = 60;

                // Available cars
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, availablePercent);

                // Rented cars
                g2d.setColor(new Color(230, 126, 34));
                g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, availablePercent,
                        rentedPercent);

                // Draw legend
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));

                // Available legend
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillRect(20, getHeight() - 60, 15, 15);
                g2d.setColor(ModernUITheme.TEXT_PRIMARY);
                int availablePercentage = (availableCars * 100) / totalCars;
                g2d.drawString("Available (" + availablePercentage + "%)", 40, getHeight() - 50);

                // Rented legend
                g2d.setColor(new Color(230, 126, 34));
                g2d.fillRect(20, getHeight() - 40, 15, 15);
                g2d.setColor(ModernUITheme.TEXT_PRIMARY);
                int rentedPercentage = (rentedCars * 100) / totalCars;
                g2d.drawString("Rented (" + rentedPercentage + "%)", 40, getHeight() - 30);
            }
        };

        chart.setPreferredSize(new Dimension(400, 200));
        chart.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chart.setOpaque(false);

        return chart;
    }

    private JPanel createRecentTransactions() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Header
        JLabel header = new JLabel("📋 Recent Transactions");
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(ModernUITheme.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(header, BorderLayout.NORTH);

        // Get real data from database
        List<Rental> rentals = new ArrayList<>();
        List<Car> cars = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        try {
            rentals = rentalService.getAllRentals();
            cars = carService.getAllCars();
            customers = customerService.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Transactions list with better layout
        JPanel transactionsList = new JPanel();
        transactionsList.setLayout(new BoxLayout(transactionsList, BoxLayout.Y_AXIS));
        transactionsList.setOpaque(false);

        // Get recent transactions (last 4)
        List<Rental> recentRentals = rentals.stream()
                .sorted((r1, r2) -> r2.getStartDate().compareTo(r1.getStartDate()))
                .limit(4)
                .collect(java.util.stream.Collectors.toList());

        for (Rental rental : recentRentals) {
            // Find car and customer details
            Car car = cars.stream()
                    .filter(c -> c.getId() == rental.getCarId())
                    .findFirst()
                    .orElse(new Car());

            Customer customer = customers.stream()
                    .filter(c -> c.getId() == rental.getCustomerId())
                    .findFirst()
                    .orElse(new Customer());

            // Calculate amount using actual total cost
            int amount = rental.getTotalCost() != null ? rental.getTotalCost().intValue() : 0;

            // Format time
            String timeAgo = formatTimeAgo(rental.getStartDate());

            JPanel transactionItem = createTransactionItem(
                    "🚗 " + (car.getModel() != null ? car.getModel() : "Unknown Car"),
                    customer.getName() != null ? customer.getName() : "Unknown Customer",
                    "$" + amount,
                    timeAgo);

            transactionsList.add(transactionItem);
            transactionsList.add(Box.createVerticalStrut(8)); // Consistent spacing
        }

        // If no transactions, show placeholder
        if (recentRentals.isEmpty()) {
            JPanel placeholder = createTransactionItem("🚗 No recent transactions", "No data available", "$0", "N/A");
            transactionsList.add(placeholder);
        }

        // Add flexible space at bottom
        transactionsList.add(Box.createVerticalGlue());

        panel.add(transactionsList, BorderLayout.CENTER);

        return panel;
    }

    private String formatTimeAgo(java.time.LocalDate rentalDate) {
        try {
            if (rentalDate == null)
                return "Unknown";

            java.time.LocalDate now = java.time.LocalDate.now();
            long days = java.time.temporal.ChronoUnit.DAYS.between(rentalDate, now);

            if (days == 0)
                return "Today";
            else if (days == 1)
                return "1 day ago";
            else if (days < 7)
                return days + " days ago";
            else if (days < 30)
                return (days / 7) + " weeks ago";
            else
                return (days / 30) + " months ago";
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private JPanel createTransactionItem(String car, String customer, String amount, String time) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Fixed height for consistency

        // Background
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(248, 249, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(new Color(222, 226, 230));
                g2d.setStroke(new java.awt.BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        background.setLayout(new BorderLayout());
        background.setOpaque(false);

        // Content
        JLabel carLabel = new JLabel(car);
        carLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        carLabel.setForeground(ModernUITheme.TEXT_PRIMARY);

        JLabel customerLabel = new JLabel(customer);
        customerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        customerLabel.setForeground(ModernUITheme.TEXT_SECONDARY);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        amountLabel.setForeground(new Color(46, 204, 113));

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(ModernUITheme.TEXT_SECONDARY);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 10));
        leftPanel.add(carLabel, BorderLayout.NORTH);
        leftPanel.add(customerLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 15));
        rightPanel.add(amountLabel, BorderLayout.NORTH);
        rightPanel.add(timeLabel, BorderLayout.CENTER);

        background.add(leftPanel, BorderLayout.WEST);
        background.add(rightPanel, BorderLayout.EAST);

        item.add(background, BorderLayout.CENTER);
        return item;
    }

    private JPanel createSystemAlerts() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Header
        JLabel header = new JLabel("⚠ System Alerts");
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(ModernUITheme.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(header, BorderLayout.NORTH);

        // Get real data from database
        List<Car> cars = new ArrayList<>();
        List<Rental> rentals = new ArrayList<>();

        try {
            cars = carService.getAllCars();
            rentals = rentalService.getAllRentals();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Alerts list with better layout
        JPanel alertsList = new JPanel();
        alertsList.setLayout(new BoxLayout(alertsList, BoxLayout.Y_AXIS));
        alertsList.setOpaque(false);

        // Generate real alerts based on data
        List<String[]> alerts = generateSystemAlerts(cars, rentals);

        for (String[] alert : alerts) {
            JPanel alertItem = createAlertItem(alert[0], alert[1], alert[2]);
            alertsList.add(alertItem);
            alertsList.add(Box.createVerticalStrut(8)); // Consistent spacing
        }

        // If no alerts, show system status
        if (alerts.isEmpty()) {
            JPanel placeholder = createAlertItem("🟢 Low", "System running smoothly", "Just now");
            alertsList.add(placeholder);
        }

        // Add flexible space at bottom
        alertsList.add(Box.createVerticalGlue());

        panel.add(alertsList, BorderLayout.CENTER);

        return panel;
    }

    private List<String[]> generateSystemAlerts(List<Car> cars, List<Rental> rentals) {
        List<String[]> alerts = new ArrayList<>();

        // Check for overdue rentals
        long overdueRentals = rentals.stream()
                .filter(r -> "ACTIVE".equals(r.getStatus()))
                .filter(r -> {
                    try {
                        java.time.LocalDate startDate = r.getStartDate();
                        if (startDate != null) {
                            long days = r.getNumberOfDays();
                            java.time.LocalDate dueDate = startDate.plusDays(days);
                            return java.time.LocalDate.now().isAfter(dueDate);
                        }
                        return false;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        if (overdueRentals > 0) {
            alerts.add(new String[] { "● High", overdueRentals + " overdue rental(s)", "1 hour ago" });
        }

        // Check for low fleet availability
        long activeRentals = rentals.stream()
                .filter(r -> "ACTIVE".equals(r.getStatus()))
                .count();

        int totalCars = cars.size();
        if (totalCars > 0 && (double) activeRentals / totalCars > 0.8) {
            alerts.add(new String[] { "● Medium", "Low fleet availability", "2 hours ago" });
        }

        // Check for system health
        if (cars.isEmpty()) {
            alerts.add(new String[] { "● High", "No cars in fleet", "Just now" });
        }

        // Check for recent activity
        long recentRentals = rentals.stream()
                .filter(r -> {
                    try {
                        java.time.LocalDate startDate = r.getStartDate();
                        if (startDate != null) {
                            return java.time.temporal.ChronoUnit.DAYS.between(startDate,
                                    java.time.LocalDate.now()) <= 1;
                        }
                        return false;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        if (recentRentals > 0) {
            alerts.add(new String[] { "● Low", recentRentals + " new rental(s) today", "Just now" });
        }

        // Limit to 4 most important alerts
        return alerts.stream().limit(4).collect(java.util.stream.Collectors.toList());
    }

    private JPanel createAlertItem(String priority, String message, String time) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Fixed height for consistency

        // Background
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(248, 249, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(new Color(222, 226, 230));
                g2d.setStroke(new java.awt.BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        background.setLayout(new BorderLayout());
        background.setOpaque(false);

        // Content
        JLabel priorityLabel = new JLabel(priority);
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        if (priority.contains("High")) {
            priorityLabel.setForeground(new Color(231, 76, 60)); // Red for high priority
        } else if (priority.contains("Medium")) {
            priorityLabel.setForeground(new Color(243, 156, 18)); // Orange for medium priority
        } else {
            priorityLabel.setForeground(new Color(46, 204, 113)); // Green for low priority
        }

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        messageLabel.setForeground(ModernUITheme.TEXT_PRIMARY);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(ModernUITheme.TEXT_SECONDARY);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 10));
        leftPanel.add(priorityLabel, BorderLayout.NORTH);
        leftPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 15));
        rightPanel.add(timeLabel, BorderLayout.CENTER);

        background.add(leftPanel, BorderLayout.WEST);
        background.add(rightPanel, BorderLayout.EAST);

        item.add(background, BorderLayout.CENTER);
        return item;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Create the actual user management content by extracting from
        // UserManagementFrame
        UserManagementFrame userFrame = new UserManagementFrame();

        // Get the content panel from UserManagementFrame
        JPanel contentPanel = (JPanel) userFrame.getContentPane().getComponent(0);

        // Remove the header panel (first component) to avoid duplicate headers
        if (contentPanel.getComponentCount() > 0) {
            contentPanel.remove(0); // Remove the header panel
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        // Find and modify the back button to navigate to dashboard instead of closing
        modifyBackButton(contentPanel, this::showDashboard);

        // Remove the frame's window decorations and add to our panel
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCarManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Create the actual car management content by extracting from
        // CarManagementFrame
        CarManagementFrame carFrame = new CarManagementFrame();

        // Get the content panel from CarManagementFrame
        JPanel contentPanel = (JPanel) carFrame.getContentPane().getComponent(0);

        // Remove the header panel (first component) to avoid duplicate headers
        if (contentPanel.getComponentCount() > 0) {
            contentPanel.remove(0); // Remove the header panel
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        // Find and modify the back button to navigate to dashboard instead of closing
        modifyBackButton(contentPanel, this::showDashboard);

        // Remove the frame's window decorations and add to our panel
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCustomerManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Create the actual customer management content by extracting from
        // CustomerManagementFrame
        CustomerManagementFrame customerFrame = new CustomerManagementFrame();

        // Get the content panel from CustomerManagementFrame
        JPanel contentPanel = (JPanel) customerFrame.getContentPane().getComponent(0);

        // Remove the header panel (first component) to avoid duplicate headers
        if (contentPanel.getComponentCount() > 0) {
            contentPanel.remove(0); // Remove the header panel
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        // Find and modify the back button to navigate to dashboard instead of closing
        modifyBackButton(contentPanel, this::showDashboard);

        // Remove the frame's window decorations and add to our panel
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRentalManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Create the actual rental management content by extracting from
        // RentalManagementFrame
        RentalManagementFrame rentalFrame = new RentalManagementFrame();

        // Set callback to refresh dashboard statistics
        rentalFrame.setRefreshCallback(this::refreshStatistics);

        // Get the content panel from RentalManagementFrame
        JPanel contentPanel = (JPanel) rentalFrame.getContentPane().getComponent(0);

        // Remove the header panel (first component) to avoid duplicate headers
        if (contentPanel.getComponentCount() > 0) {
            contentPanel.remove(0); // Remove the header panel
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        // Find and modify the back button to navigate to dashboard instead of closing
        modifyBackButton(contentPanel, this::showDashboard);

        // Remove the frame's window decorations and add to our panel
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Create the actual reports content by extracting from ReportsFrame
        ReportsFrame reportsFrame = new ReportsFrame();

        // Get the content panel from ReportsFrame
        JPanel contentPanel = (JPanel) reportsFrame.getContentPane().getComponent(0);

        // Remove the header panel (first component) to avoid duplicate headers
        if (contentPanel.getComponentCount() > 0) {
            contentPanel.remove(0); // Remove the header panel
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        // Find and modify the back button to navigate to dashboard instead of closing
        modifyBackButton(contentPanel, this::showDashboard);

        // Remove the frame's window decorations and add to our panel
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Recursively find and modify back buttons in the component hierarchy
     */
    private void modifyBackButton(Container container, Runnable dashboardCallback) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().contains("Back to Dashboard")) {
                    // Remove existing action listeners and add new one
                    for (ActionListener listener : button.getActionListeners()) {
                        button.removeActionListener(listener);
                    }
                    button.addActionListener(e -> dashboardCallback.run());
                }
            } else if (comp instanceof Container) {
                modifyBackButton((Container) comp, dashboardCallback);
            }
        }
    }

    // Method to update header text
    private void updateHeader(String text) {
        if (welcomeLabel != null) {
            welcomeLabel.setText(text);
            welcomeLabel.revalidate();
            welcomeLabel.repaint();
        }
    }

    /**
     * Get the current admin's name for welcome message
     */
    private String getAdminWelcomeMessage() {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getUsername();
            if (username != null && !username.trim().isEmpty()) {
                return "👨‍💼 Welcome, " + username;
            }
        }
        // Fallback if no user data available
        return "👨‍💼 Welcome, Administrator";
    }

    // Navigation methods
    private void showDashboard() {
        cardLayout.show(mainContentPanel, "DASHBOARD");
        updateHeader(getAdminWelcomeMessage());
    }

    private void showUserManagement() {
        cardLayout.show(mainContentPanel, "USER_MANAGEMENT");
        updateHeader("👥 User Management");
    }

    private void showCarManagement() {
        cardLayout.show(mainContentPanel, "CAR_MANAGEMENT");
        updateHeader("🚗 Car Management");
    }

    private void showCustomerManagement() {
        cardLayout.show(mainContentPanel, "CUSTOMER_MANAGEMENT");
        updateHeader("👤 Customer Management");
    }

    private void showRentalManagement() {
        cardLayout.show(mainContentPanel, "RENTAL_MANAGEMENT");
        updateHeader("📋 Rental Management");
    }

    private void showReports() {
        cardLayout.show(mainContentPanel, "REPORTS");
        updateHeader("📊 Reports & Analytics");
    }

    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 20, 0));
        statsPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        statsPanel.setOpaque(false);

        try {
            // Get statistics
            List<Car> cars = carService.getAllCars();
            List<Customer> customers = customerService.getAllCustomers();
            List<Rental> rentals = rentalService.getAllRentals();
            List<User> users = userService.getAllUsers();

            // Create stat cards
            statsPanel
                    .add(createStatCard("🚗", "Total Cars", String.valueOf(cars.size()), ModernUITheme.SUCCESS_COLOR));
            statsPanel.add(createStatCard("👥", "Total Customers", String.valueOf(customers.size()),
                    ModernUITheme.PRIMARY_COLOR));
            statsPanel.add(createStatCard("📋", "Active Rentals",
                    String.valueOf(rentals.stream().filter(r -> "ACTIVE".equals(r.getStatus())).count()),
                    ModernUITheme.ACCENT_COLOR));
            statsPanel.add(
                    createStatCard("👨‍💼", "System Users", String.valueOf(users.size()), ModernUITheme.WARNING_COLOR));
            statsPanel.add(createStatCard("💰", "Total Revenue",
                    "$" + rentalService.getTotalRevenue(), // Use actual revenue calculation
                    ModernUITheme.INFO_COLOR));

        } catch (SQLException e) {
            // If database error, show placeholder stats
            statsPanel.add(createStatCard("🚗", "Total Cars", "0", ModernUITheme.SUCCESS_COLOR));
            statsPanel.add(createStatCard("👥", "Total Customers", "0", ModernUITheme.PRIMARY_COLOR));
            statsPanel.add(createStatCard("📋", "Active Rentals", "0", ModernUITheme.ACCENT_COLOR));
            statsPanel.add(createStatCard("👨‍💼", "System Users", "0", ModernUITheme.WARNING_COLOR));
            statsPanel.add(createStatCard("💰", "Total Revenue", "$0", ModernUITheme.INFO_COLOR));
        }

        return statsPanel;
    }

    private JPanel createStatCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 15, 15);

                // Draw main card with gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(248, 249, 250));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);

                // Draw accent border
                g2d.setColor(color);
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);

                // Draw icon
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                FontMetrics fm = g2d.getFontMetrics();
                int iconX = (getWidth() - fm.stringWidth(icon)) / 2;
                g2d.setColor(color);
                g2d.drawString(icon, iconX, 35);

                // Draw value
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
                fm = g2d.getFontMetrics();
                int valueX = (getWidth() - fm.stringWidth(value)) / 2;
                g2d.setColor(ModernUITheme.TEXT_PRIMARY);
                g2d.drawString(value, valueX, 65);

                // Draw title
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2d.setColor(ModernUITheme.TEXT_SECONDARY);
                fm = g2d.getFontMetrics();
                int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                g2d.drawString(title, titleX, 85);
            }
        };

        card.setPreferredSize(new Dimension(200, 100));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setOpaque(false);

        return card;
    }

    private void setFrameProperties() {
        setTitle("Admin Dashboard - Car Rental Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    /**
     * Refresh the statistics panel
     */
    public void refreshStatistics() {
        // Remove old statistics panel
        if (dashboardPanel != null) {
            dashboardPanel.removeAll();

            // Recreate the dashboard panel
            dashboardPanel = createDashboardPanel();

            // Update the card layout
            mainContentPanel.remove(dashboardPanel);
            mainContentPanel.add(dashboardPanel, "DASHBOARD");

            // Force revalidation and repaint
            dashboardPanel.revalidate();
            dashboardPanel.repaint();
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        }

        // Also refresh the welcome message
        updateHeader(getAdminWelcomeMessage());
    }
}
