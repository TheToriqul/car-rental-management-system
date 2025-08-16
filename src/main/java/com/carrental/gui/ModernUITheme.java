package com.carrental.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Modern UI Theme utility class for consistent styling across the application
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class ModernUITheme {

  // Color Palette
  public static final Color PRIMARY_COLOR = new Color(52, 152, 219); // Blue
  public static final Color PRIMARY_DARK = new Color(41, 128, 185); // Darker Blue
  public static final Color SECONDARY_COLOR = new Color(46, 204, 113); // Green
  public static final Color ACCENT_COLOR = new Color(155, 89, 182); // Purple
  public static final Color WARNING_COLOR = new Color(241, 196, 15); // Yellow
  public static final Color DANGER_COLOR = new Color(231, 76, 60); // Red
  public static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
  public static final Color INFO_COLOR = new Color(52, 152, 219); // Blue

  // Background Colors
  public static final Color BACKGROUND_LIGHT = new Color(248, 249, 250); // Light Gray
  public static final Color BACKGROUND_WHITE = new Color(255, 255, 255); // White
  public static final Color BACKGROUND_DARK = new Color(52, 73, 94); // Dark Gray

  // Text Colors
  public static final Color TEXT_PRIMARY = new Color(44, 62, 80); // Dark Gray
  public static final Color TEXT_SECONDARY = new Color(127, 140, 141); // Medium Gray
  public static final Color TEXT_LIGHT = new Color(255, 255, 255); // White

  // Border Colors
  public static final Color BORDER_LIGHT = new Color(236, 240, 241); // Light Gray
  public static final Color BORDER_MEDIUM = new Color(189, 195, 199); // Medium Gray

  // Fonts
  public static final Font FONT_PRIMARY = new Font("Segoe UI", Font.PLAIN, 14);
  public static final Font FONT_PRIMARY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
  public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
  public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
  public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

  // Borders
  public static final Border BORDER_ROUNDED = BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(BORDER_LIGHT, 1),
      new EmptyBorder(10, 16, 10, 16));

  public static final Border BORDER_FOCUS = BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
      new EmptyBorder(9, 15, 9, 15));

  // Modern rounded border for buttons
  public static final Border BUTTON_ROUNDED = BorderFactory.createEmptyBorder(12, 20, 12, 20);

  /**
   * Check if text contains emoji characters
   */
  private static boolean containsEmoji(String text) {
    return text.matches(".*[\\uD800-\\uDFFF].*") ||
        text.contains("🔄") || text.contains("⬅️") || text.contains("➕") ||
        text.contains("✏️") || text.contains("🗑️") || text.contains("🔍") ||
        text.contains("✅") || text.contains("👁️") || text.contains("👥") ||
        text.contains("🚗") || text.contains("📊") || text.contains("💾") ||
        text.contains("❌");
  }

  /**
   * Get appropriate font for text rendering
   */
  private static Font getAppropriateFont(String text) {
    if (containsEmoji(text)) {
      // Try multiple emoji fonts for better compatibility
      Font[] emojiFonts = {
          new Font("Segoe UI Emoji", Font.PLAIN, 14),
          new Font("Apple Color Emoji", Font.PLAIN, 14),
          new Font("Noto Color Emoji", Font.PLAIN, 14),
          new Font("DejaVu Sans", Font.PLAIN, 14)
      };

      for (Font font : emojiFonts) {
        if (font.canDisplayUpTo(text) == -1) {
          return font;
        }
      }
      return emojiFonts[0]; // Fallback to first emoji font
    }
    return FONT_PRIMARY_BOLD;
  }

  /**
   * Replace emoji characters with simple text alternatives for better
   * compatibility
   */
  private static String replaceEmojiWithText(String text) {
    return text
        .replace("🔄", "↻")
        .replace("⬅️", "←")
        .replace("➕", "+")
        .replace("✏️", "✎")
        .replace("🗑️", "🗑")
        .replace("🔍", "🔎")
        .replace("✅", "✓")
        .replace("👁️", "👁")
        .replace("👥", "👤")
        .replace("🚗", "🚙")
        .replace("📊", "📈")
        .replace("💾", "💿")
        .replace("❌", "✗");
  }

  /**
   * Create a modern button with universal styling
   */
  public static JButton createModernButton(String text, String type) {
    // Replace emoji characters with simpler alternatives for better compatibility
    String displayText = replaceEmojiWithText(text);
    JButton button = new JButton(displayText);

    // Calculate proper button size based on text length
    Font sizeFont = getAppropriateFont(text);
    FontMetrics fm = button.getFontMetrics(sizeFont);
    int textWidth = fm.stringWidth(text);
    int minWidth = Math.max(130, textWidth + 40); // Add padding for text
    button.setPreferredSize(new Dimension(minWidth, 45));

    button.setFont(FONT_PRIMARY_BOLD);
    button.setForeground(TEXT_LIGHT);
    button.setBorder(BUTTON_ROUNDED);
    button.setFocusPainted(false);
    button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    button.setOpaque(false);
    button.setContentAreaFilled(false);

    // Set button colors based on type
    Color baseColor, hoverColor, pressedColor, shadowColor;

    switch (type.toLowerCase()) {
      case "primary":
        baseColor = PRIMARY_COLOR;
        hoverColor = new Color(41, 128, 185);
        pressedColor = new Color(31, 97, 141);
        shadowColor = new Color(41, 128, 185, 80);
        break;
      case "success":
        baseColor = SUCCESS_COLOR;
        hoverColor = new Color(39, 174, 96);
        pressedColor = new Color(27, 123, 68);
        shadowColor = new Color(39, 174, 96, 80);
        break;
      case "danger":
        baseColor = DANGER_COLOR;
        hoverColor = new Color(192, 57, 43);
        pressedColor = new Color(146, 43, 33);
        shadowColor = new Color(192, 57, 43, 80);
        break;
      case "warning":
        baseColor = WARNING_COLOR;
        hoverColor = new Color(230, 126, 34);
        pressedColor = new Color(211, 84, 0);
        shadowColor = new Color(230, 126, 34, 80);
        break;
      case "info":
        baseColor = INFO_COLOR;
        hoverColor = new Color(41, 128, 185);
        pressedColor = new Color(31, 97, 141);
        shadowColor = new Color(41, 128, 185, 80);
        break;
      default:
        baseColor = PRIMARY_COLOR;
        hoverColor = new Color(41, 128, 185);
        pressedColor = new Color(31, 97, 141);
        shadowColor = new Color(41, 128, 185, 80);
    }

    // Store colors as client properties for the custom UI
    button.putClientProperty("baseColor", baseColor);
    button.putClientProperty("hoverColor", hoverColor);
    button.putClientProperty("pressedColor", pressedColor);
    button.putClientProperty("shadowColor", shadowColor);

    // Apply custom modern UI
    button.setUI(new ModernButtonUI());

    return button;
  }

  /**
   * Custom Button UI for modern appearance
   */
  private static class ModernButtonUI extends javax.swing.plaf.basic.BasicButtonUI {

    @Override
    public void paint(Graphics g, JComponent c) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      AbstractButton button = (AbstractButton) c;
      ButtonModel model = button.getModel();

      // Get colors from client properties
      Color baseColor = (Color) button.getClientProperty("baseColor");
      Color hoverColor = (Color) button.getClientProperty("hoverColor");
      Color pressedColor = (Color) button.getClientProperty("pressedColor");
      Color shadowColor = (Color) button.getClientProperty("shadowColor");

      if (baseColor == null)
        baseColor = PRIMARY_COLOR;
      if (hoverColor == null)
        hoverColor = PRIMARY_DARK;
      if (pressedColor == null)
        pressedColor = new Color(31, 97, 141);
      if (shadowColor == null)
        shadowColor = new Color(41, 128, 185, 80);

      int width = c.getWidth();
      int height = c.getHeight();
      int arc = 25; // Corner radius

      // Determine current state color
      Color currentColor;
      if (model.isPressed()) {
        currentColor = pressedColor;
      } else if (model.isRollover()) {
        currentColor = hoverColor;
      } else {
        currentColor = baseColor;
      }

      // Draw shadow (only if not pressed)
      if (!model.isPressed()) {
        g2d.setColor(shadowColor);
        g2d.fill(new RoundRectangle2D.Float(3, 3, width - 3, height - 3, arc, arc));
      }

      // Create gradient for button
      GradientPaint gradient;
      if (model.isPressed()) {
        gradient = new GradientPaint(0, 0, currentColor.darker(), 0, height, currentColor);
      } else {
        gradient = new GradientPaint(0, 0, currentColor, 0, height, currentColor.darker());
      }
      g2d.setPaint(gradient);

      // Draw main button body
      int buttonY = model.isPressed() ? 1 : 0;
      int buttonHeight = model.isPressed() ? height - 1 : height - 3;
      g2d.fill(new RoundRectangle2D.Float(0, buttonY, width - 3, buttonHeight, arc, arc));

      // Add highlight effect
      if (!model.isPressed()) {
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.fill(new RoundRectangle2D.Float(2, 2, width - 7, (height - 5) / 2, arc, arc));
      }

      // Draw border
      g2d.setColor(currentColor.darker());
      g2d.setStroke(new java.awt.BasicStroke(1.5f));
      g2d.draw(new RoundRectangle2D.Float(0, buttonY, width - 3, buttonHeight, arc, arc));

      // Draw text - account for button drawing area
      g2d.setColor(button.getForeground());

      String text = button.getText();
      Font drawFont = getAppropriateFont(text);
      g2d.setFont(drawFont);
      FontMetrics fm = g2d.getFontMetrics(drawFont);

      // Calculate text position within the actual button area (excluding shadow)
      int buttonWidth = width - 3; // Account for shadow
      int textX = (buttonWidth - fm.stringWidth(text)) / 2;
      int textY = (buttonHeight + fm.getAscent() - fm.getDescent()) / 2 + buttonY; // Add buttonY offset

      // Adjust text position if button is pressed
      if (model.isPressed()) {
        textX += 1;
        textY += 1;
      }

      g2d.drawString(text, textX, textY);
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
      // This is handled in the main paint method
    }
  }

  /**
   * Apply modern styling to a panel
   */
  public static void stylePanel(JPanel panel) {
    panel.setBackground(BACKGROUND_WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
  }

  /**
   * Create a modern gradient panel
   */
  public static JPanel createGradientPanel(Color color1, Color color2) {
    return new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };
  }

  /**
   * Create a modern card panel
   */
  public static JPanel createCardPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(BACKGROUND_WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_LIGHT, 1),
        new EmptyBorder(15, 15, 15, 15)));
    return panel;
  }

  /**
   * Apply modern styling to a component
   */
  public static void styleComponent(JComponent component) {
    component.setFont(FONT_PRIMARY);
    component.setBackground(BACKGROUND_WHITE);
    component.setForeground(TEXT_PRIMARY);
  }

  // Legacy methods for backward compatibility
  public static void styleButton(JButton button) {
    // This method is now deprecated, use createModernButton instead
    button = createModernButton(button.getText(), "primary");
  }

  public static void stylePrimaryButton(JButton button) {
    // This method is now deprecated, use createModernButton instead
    button = createModernButton(button.getText(), "primary");
  }

  public static void styleSuccessButton(JButton button) {
    // This method is now deprecated, use createModernButton instead
    button = createModernButton(button.getText(), "success");
  }

  public static void styleDangerButton(JButton button) {
    // This method is now deprecated, use createModernButton instead
    button = createModernButton(button.getText(), "danger");
  }
}
