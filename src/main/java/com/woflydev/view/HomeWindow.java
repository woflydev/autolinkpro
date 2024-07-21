package com.woflydev.view;

import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;
import com.woflydev.view.createbooking.BookingWindow;
import com.woflydev.view.manage.ManageBookingsWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.woflydev.model.Globals.PRIVILEGE_STAFF;
import static com.woflydev.controller.UserUtils.hasPrivilege;
import java.util.ArrayList;
import java.util.List;

public class HomeWindow extends JFrame implements ActionListener {
    public static HomeWindow instance = null;
    private static List<JFrame> openWindows = new ArrayList<>();

    private JButton createBookingButton;
    private JButton manageBookingsButton;
    private JButton settingsButton;
    private JButton adminButton;
    private JButton logoutButton;
    private JLabel emailLabel;

    public HomeWindow() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Change to DISPOSE_ON_CLOSE
        setLayout(new BorderLayout());

        // Main panel for buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Home", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Allow the heading to occupy some vertical space
        mainPanel.add(headingLabel, gbc);

        // Create buttons
        createBookingButton = new JButton("Create Booking");
        manageBookingsButton = new JButton("Manage Bookings");
        settingsButton = new JButton("Settings");
        adminButton = new JButton("Admin");
        logoutButton = new JButton("Logout");

        createBookingButton.setPreferredSize(new Dimension(150, 40));
        manageBookingsButton.setPreferredSize(new Dimension(150, 40));
        settingsButton.setPreferredSize(new Dimension(150, 40));
        adminButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setPreferredSize(new Dimension(100, 30)); // Smaller logout button

        createBookingButton.setBackground(new Color(0, 120, 215));
        manageBookingsButton.setBackground(new Color(0, 120, 215));
        settingsButton.setBackground(new Color(0, 120, 215));
        adminButton.setBackground(new Color(0, 120, 215));
        logoutButton.setBackground(new Color(255, 69, 58)); // Red color for logout

        createBookingButton.setForeground(Color.WHITE);
        manageBookingsButton.setForeground(Color.WHITE);
        settingsButton.setForeground(Color.WHITE);
        adminButton.setForeground(Color.WHITE);
        logoutButton.setForeground(Color.WHITE);

        createBookingButton.setFocusPainted(false);
        manageBookingsButton.setFocusPainted(false);
        settingsButton.setFocusPainted(false);
        adminButton.setFocusPainted(false);
        logoutButton.setFocusPainted(false);

        createBookingButton.addActionListener(this);
        manageBookingsButton.addActionListener(this);
        settingsButton.addActionListener(this);
        adminButton.addActionListener(this);
        logoutButton.addActionListener(this);

        gbc.gridwidth = 1; // Reset gridwidth for buttons

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0; // Reset weighty for buttons
        mainPanel.add(createBookingButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(manageBookingsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(settingsButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;

        if (hasPrivilege(Globals.CURRENT_USER_EMAIL, PRIVILEGE_STAFF)) {
            mainPanel.add(adminButton, gbc);
        }

        add(mainPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        footerPanel.setLayout(new BorderLayout()); // Use BorderLayout

        emailLabel = new JLabel("Logged in as: " + Globals.CURRENT_USER_EMAIL);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(emailLabel, BorderLayout.WEST); // Align email label to the left

        // Adjust logoutButton size and position
        logoutButton.setPreferredSize(new Dimension(100, 30)); // Smaller button size
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutButton); // Add logout button to a panel

        footerPanel.add(logoutPanel, BorderLayout.EAST); // Align logout button to the right

        add(footerPanel, BorderLayout.SOUTH);

        // Set window size and properties
        setSize(500, 350); // Increased height
        setLocationRelativeTo(null);
        setResizable(false); // Disable resizing

        // Add this window to the list of open windows
        openWindows.add(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createBookingButton) {
            BookingWindow.open();
        } else if (e.getSource() == manageBookingsButton) {
            ManageBookingsWindow.open();
        } else if (e.getSource() == settingsButton) {
            System.out.println("Settings button clicked");
        } else if (e.getSource() == adminButton) {
            AdminToolsWindow.open();
        } else if (e.getSource() == logoutButton) {
            handleLogout();
        }
    }

    private void handleLogout() {
        int response = WindowUtils.confirmationBox("Are you sure you want to log out?");
        if (response == JOptionPane.YES_OPTION) {
            Globals.CURRENT_USER_EMAIL = null;
            WindowUtils.closeAllWindows();
            LoginWindow.open();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
        // Remove this window from the list of open windows
        openWindows.remove(this);
    }

    public static void open() {
        if (instance == null) {
            instance = new HomeWindow();
            WindowUtils.register(instance);
        }
    }
}
