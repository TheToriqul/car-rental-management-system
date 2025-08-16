package com.carrental.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.carrental.service.AuthenticationService;

/**
 * Login frame for user authentication
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private AuthenticationService authService;

    public LoginFrame() {
        authService = new AuthenticationService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Create modern buttons with proper styling
        loginButton = ModernUITheme.createModernButton("Login", "primary");
        exitButton = ModernUITheme.createModernButton("Exit", "danger");

        // Apply modern styling to text fields
        ModernUITheme.styleComponent(usernameField);
        ModernUITheme.styleComponent(passwordField);

        // Style text fields
        usernameField.setBorder(ModernUITheme.BORDER_ROUNDED);
        passwordField.setBorder(ModernUITheme.BORDER_ROUNDED);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create beautiful gradient background with pattern
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Create beautiful gradient from blue to purple
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(52, 152, 219),
                        getWidth(), getHeight(), new Color(155, 89, 182));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add subtle pattern overlay
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < getWidth(); i += 30) {
                    for (int j = 0; j < getHeight(); j += 30) {
                        g2d.fillOval(i, j, 2, 2);
                    }
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Create main content container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);

        // Left side - Project Information Panel
        JPanel leftPanel = createProjectInfoPanel();

        // Right side - Login Form Panel
        JPanel rightPanel = createLoginFormPanel();

        // Add panels to main container
        mainContainer.add(leftPanel, BorderLayout.WEST);
        mainContainer.add(rightPanel, BorderLayout.CENTER);

        // Add main container to background
        backgroundPanel.add(mainContainer, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private JPanel createProjectInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(255, 255, 255, 200));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Enhanced shadow effect
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

                // Draw main panel
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 20, 20);
            }
        };
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setOpaque(false);
        shadowPanel.add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Project Logo with INTI Logo
        JPanel logoPanel = new JPanel() {
            private Image intiLogo;

            {
                try {
                    // Load INTI logo
                    File logoFile = new File("assets/inti logo.png");
                    if (logoFile.exists()) {
                        intiLogo = ImageIO.read(logoFile);
                    }
                } catch (IOException e) {
                    System.err.println("Could not load INTI logo: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Draw INTI logo if available, otherwise fallback to car icon
                if (intiLogo != null) {
                    // Calculate dimensions to maintain aspect ratio
                    double logoAspectRatio = (double) intiLogo.getWidth(null) / intiLogo.getHeight(null);
                    int maxWidth = getWidth() - 20;
                    int maxHeight = getHeight() - 20;

                    int logoWidth, logoHeight;
                    if (maxWidth / logoAspectRatio <= maxHeight) {
                        // Width is the limiting factor
                        logoWidth = maxWidth;
                        logoHeight = (int) (maxWidth / logoAspectRatio);
                    } else {
                        // Height is the limiting factor
                        logoHeight = maxHeight;
                        logoWidth = (int) (maxHeight * logoAspectRatio);
                    }

                    // Center the logo
                    int x = (getWidth() - logoWidth) / 2;
                    int y = (getHeight() - logoHeight) / 2;
                    g2d.drawImage(intiLogo, x, y, logoWidth, logoHeight, null);
                } else {
                    // Fallback to car icon
                    g2d.setColor(ModernUITheme.PRIMARY_COLOR);
                    g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth("🚗")) / 2;
                    int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2d.drawString("🚗", textX, textY);
                }
            }
        };
        logoPanel.setPreferredSize(new Dimension(200, 120));
        logoPanel.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 15, 25, 15);
        panel.add(logoPanel, gbc);

        // Project Title
        JLabel titleLabel = new JLabel("Car Rental Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ModernUITheme.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 10, 15);
        panel.add(titleLabel, gbc);

        // University Info
        JLabel uniLabel = new JLabel("INTI International University");
        uniLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        uniLabel.setForeground(ModernUITheme.ACCENT_COLOR);
        uniLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 15, 5, 15);
        panel.add(uniLabel, gbc);

        JLabel courseLabel = new JLabel("PRG 2201E - Object Oriented Programming - Group 3");
        courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseLabel.setForeground(ModernUITheme.TEXT_SECONDARY);
        courseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 15, 25, 15);
        panel.add(courseLabel, gbc);

        // Project Features
        JLabel featuresTitle = new JLabel("🎯 Project Features");
        featuresTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        featuresTitle.setForeground(ModernUITheme.TEXT_PRIMARY);
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 15, 15, 15);
        panel.add(featuresTitle, gbc);

        String[] features = {
                "🔐 Role-based Authentication",
                "🚗 Complete Car Management",
                "👥 Customer Management",
                "📋 Rental Management",
                "📊 Reports & Analytics",
                "🔍 Advanced Search",
                "💾 SQLite Database",
                "🎨 Modern UI Design"
        };

        for (int i = 0; i < features.length; i++) {
            JLabel featureLabel = new JLabel(features[i]);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            featureLabel.setForeground(ModernUITheme.TEXT_SECONDARY);
            gbc.gridy = 5 + i;
            gbc.insets = new Insets(3, 15, 3, 15);
            panel.add(featureLabel, gbc);
        }

        // Group Info
        JLabel groupTitle = new JLabel("👥 Group 3 - Team Members");
        groupTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        groupTitle.setForeground(ModernUITheme.TEXT_PRIMARY);
        gbc.gridy = 5 + features.length;
        gbc.insets = new Insets(20, 15, 10, 15);
        panel.add(groupTitle, gbc);

        // First team member
        JLabel member1Name = new JLabel("Md Toriqul Islam");
        member1Name.setFont(new Font("Segoe UI", Font.BOLD, 13));
        member1Name.setForeground(ModernUITheme.ACCENT_COLOR);
        gbc.gridy = 6 + features.length;
        gbc.insets = new Insets(0, 15, 2, 15);
        panel.add(member1Name, gbc);

        JLabel member1Id = new JLabel("Student ID: I24029037");
        member1Id.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        member1Id.setForeground(ModernUITheme.TEXT_SECONDARY);
        gbc.gridy = 7 + features.length;
        gbc.insets = new Insets(0, 15, 8, 15);
        panel.add(member1Id, gbc);

        // Second team member
        JLabel member2Name = new JLabel("Jenesh Nair");
        member2Name.setFont(new Font("Segoe UI", Font.BOLD, 13));
        member2Name.setForeground(ModernUITheme.ACCENT_COLOR);
        gbc.gridy = 8 + features.length;
        gbc.insets = new Insets(0, 15, 2, 15);
        panel.add(member2Name, gbc);

        JLabel member2Id = new JLabel("Student ID: I24028978");
        member2Id.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        member2Id.setForeground(ModernUITheme.TEXT_SECONDARY);
        gbc.gridy = 9 + features.length;
        gbc.insets = new Insets(0, 15, 0, 15);
        panel.add(member2Id, gbc);

        return shadowPanel;
    }

    private JPanel createLoginFormPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(ModernUITheme.BACKGROUND_WHITE);
        mainPanel.setBorder(new EmptyBorder(50, 60, 50, 60));

        // Enhanced shadow effect with multiple layers
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw multiple shadow layers for depth
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8, 25, 25);

                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 25, 25);

                g2d.setColor(new Color(0, 0, 0, 25));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 25, 25);

                // Draw main panel with gradient
                GradientPaint cardGradient = new GradientPaint(
                        0, 0, ModernUITheme.BACKGROUND_WHITE,
                        0, getHeight(), new Color(248, 249, 250));
                g2d.setPaint(cardGradient);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 25, 25);

                // Add subtle border
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 25, 25);
            }
        };
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Enhanced logo with background circle
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background circle
                g2d.setColor(new Color(52, 152, 219, 20));
                g2d.fillOval(10, 10, getWidth() - 20, getHeight() - 20);

                // Draw car icon
                g2d.setColor(ModernUITheme.PRIMARY_COLOR);
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth("🚗")) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString("🚗", textX, textY);
            }
        };
        logoPanel.setPreferredSize(new Dimension(80, 80));
        logoPanel.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 25, 10);
        mainPanel.add(logoPanel, gbc);

        // Enhanced title with gradient text effect
        JLabel titleLabel = new JLabel("Welcome Back!") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint textGradient = new GradientPaint(
                        0, 0, ModernUITheme.PRIMARY_COLOR,
                        getWidth(), 0, ModernUITheme.ACCENT_COLOR);
                g2d.setPaint(textGradient);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        titleLabel.setPreferredSize(new Dimension(300, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 8, 10);
        mainPanel.add(titleLabel, gbc);

        // Subtitle with enhanced styling
        JLabel subtitleLabel = new JLabel("Sign in to access your dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(ModernUITheme.TEXT_SECONDARY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 35, 10);
        mainPanel.add(subtitleLabel, gbc);

        // Username field with enhanced styling
        JLabel usernameLabel = new JLabel("👤 Username");
        usernameLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        usernameLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 8, 10);
        mainPanel.add(usernameLabel, gbc);

        // Enhanced username field
        usernameField.setPreferredSize(new Dimension(280, 45));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernUITheme.BORDER_LIGHT, 1),
                new EmptyBorder(12, 18, 12, 18)));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameField, gbc);

        // Password field with enhanced styling
        JLabel passwordLabel = new JLabel("🔒 Password");
        passwordLabel.setFont(ModernUITheme.FONT_PRIMARY_BOLD);
        passwordLabel.setForeground(ModernUITheme.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(passwordLabel, gbc);

        // Enhanced password field
        passwordField.setPreferredSize(new Dimension(280, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernUITheme.BORDER_LIGHT, 1),
                new EmptyBorder(12, 18, 12, 18)));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordField, gbc);

        // Enhanced buttons panel with better spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Make buttons larger for better UX
        loginButton.setPreferredSize(new Dimension(150, 55));
        exitButton.setPreferredSize(new Dimension(150, 55));

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(35, 10, 15, 10);
        mainPanel.add(buttonPanel, gbc);

        // Add footer text
        JLabel footerLabel = new JLabel("© 2025 INTI International University - PRG 2201E - Group 3");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(150, 150, 150));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 0, 10);
        mainPanel.add(footerLabel, gbc);

        return cardPanel;
    }

    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Exit button action
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Enter key in password field
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void setFrameProperties() {
        setTitle("Car Rental Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1280, 720);
        setLocationRelativeTo(null);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Attempt authentication
            boolean isAuthenticated = authService.authenticate(username, password);

            if (isAuthenticated) {
                // Get user role
                String role = authService.getCurrentUserRole();

                // Close login frame
                this.dispose();

                // Open appropriate dashboard
                if ("ADMIN".equals(role)) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new StaffDashboard().setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password. Please try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error during login: " + ex.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("Login error: " + ex.getMessage());
        }
    }
}
