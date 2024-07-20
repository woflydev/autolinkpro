package com.woflydev.view;

import com.woflydev.model.Globals;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static com.woflydev.model.Globals.PRIVILEGE_STAFF;
import static com.woflydev.controller.UserUtils.hasPrivilege;

public class HomeWindow extends JFrame implements ActionListener {
    public static HomeWindow instance = null;

    private JButton createBookingButton;
    private JButton manageBookingsButton;
    private JButton settingsButton;
    private JButton adminButton;

    public HomeWindow() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        createBookingButton.setPreferredSize(new Dimension(150, 40));
        manageBookingsButton.setPreferredSize(new Dimension(150, 40));
        settingsButton.setPreferredSize(new Dimension(150, 40));
        adminButton.setPreferredSize(new Dimension(150, 40));

        createBookingButton.setBackground(new Color(0, 120, 215));
        manageBookingsButton.setBackground(new Color(0, 120, 215));
        settingsButton.setBackground(new Color(0, 120, 215));
        adminButton.setBackground(new Color(0, 120, 215));

        createBookingButton.setForeground(Color.WHITE);
        manageBookingsButton.setForeground(Color.WHITE);
        settingsButton.setForeground(Color.WHITE);
        adminButton.setForeground(Color.WHITE);

        createBookingButton.setFocusPainted(false);
        manageBookingsButton.setFocusPainted(false);
        settingsButton.setFocusPainted(false);
        adminButton.setFocusPainted(false);

        createBookingButton.addActionListener(this);
        manageBookingsButton.addActionListener(this);
        settingsButton.addActionListener(this);
        adminButton.addActionListener(this);

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

        setSize(500, 300);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createBookingButton) {
            CreateBookingWindow.open();
        } else if (e.getSource() == manageBookingsButton) {
            System.out.println("Manage Bookings button clicked");
        } else if (e.getSource() == settingsButton) {
            System.out.println("Settings button clicked");
        } else if (e.getSource() == adminButton) {
            AdminToolsWindow.open();
        }
    }


    public static void open() {
        if (instance == null) {
            instance = new HomeWindow();
            instance.setVisible(true);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
