package com.woflydev.view;

import com.woflydev.controller.StyleUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;
import com.woflydev.view.createbooking.BookingWindow;
import com.woflydev.view.manage.ManageBookingWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.woflydev.controller.StyleUtils.BUTTON_STYLES.WARNING;
import static com.woflydev.model.Globals.PRIVILEGE_STAFF;
import static com.woflydev.controller.UserUtils.hasPrivilege;
import java.util.ArrayList;
import java.util.List;

/**
 * Main screen of the application.
 * Author: woflydev
 */
public class HomeWindow extends JFrame implements ActionListener {
    public static HomeWindow instance = null;
    private static List<JFrame> openWindows = new ArrayList<>();

    private JButton createBookingButton;
    private JButton manageBookingsButton;
    private JButton settingsButton;
    private JButton adminButton;
    private JButton logoutButton;
    private JButton aboutButton;
    private JLabel emailLabel;

    public HomeWindow() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel headingLabel = new JLabel("Home", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        createBookingButton = new JButton("Create Booking");
        manageBookingsButton = new JButton("Manage Bookings");
        settingsButton = new JButton("Settings");
        adminButton = new JButton("Admin");
        logoutButton = new JButton("Logout");
        aboutButton = new JButton("About");
        StyleUtils.applyButtonStyle(logoutButton, WARNING);

        createBookingButton.setPreferredSize(new Dimension(150, 40));
        manageBookingsButton.setPreferredSize(new Dimension(150, 40));
        settingsButton.setPreferredSize(new Dimension(150, 40));
        adminButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setPreferredSize(new Dimension(100, 30));
        aboutButton.setPreferredSize(new Dimension(100, 30));

        createBookingButton.setFocusPainted(false);
        manageBookingsButton.setFocusPainted(false);
        settingsButton.setFocusPainted(false);
        adminButton.setFocusPainted(false);
        logoutButton.setFocusPainted(false);
        aboutButton.setFocusPainted(false);

        createBookingButton.addActionListener(this);
        manageBookingsButton.addActionListener(this);
        settingsButton.addActionListener(this);
        adminButton.addActionListener(this);
        logoutButton.addActionListener(this);
        aboutButton.addActionListener(this);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        mainPanel.add(createBookingButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(manageBookingsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(settingsButton, gbc);

        if (hasPrivilege(Globals.CURRENT_USER_EMAIL, PRIVILEGE_STAFF)) {
            gbc.gridx = 1;
            gbc.gridy = 2;
            mainPanel.add(adminButton, gbc);
        }

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(aboutButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        footerPanel.setLayout(new BorderLayout());

        emailLabel = new JLabel("Logged in as: " + Globals.CURRENT_USER_EMAIL);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(emailLabel, BorderLayout.WEST);

        aboutButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.setPreferredSize(new Dimension(100, 30));
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(aboutButton);
        logoutPanel.add(logoutButton);

        footerPanel.add(logoutPanel, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        openWindows.add(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createBookingButton) {
            BookingWindow.open();
        } else if (e.getSource() == manageBookingsButton) {
            ManageBookingWindow.open();
        } else if (e.getSource() == settingsButton) {
            SettingsWindow.open();
        } else if (e.getSource() == adminButton) {
            AdminToolsWindow.open();
        } else if (e.getSource() == logoutButton) {
            handleLogout();
        } else if (e.getSource() == aboutButton) {
            WindowUtils.infoBox("""
                    Thank you for using AutoLink Pro!
                    
                    A Swing UI application built using FlatLaf.
                    Data stored using Google's gson library in the JSON format.
                    
                    Written by woflydev.
                    """);
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
        openWindows.remove(this);
    }

    public static void open() {
        if (instance == null) {
            instance = new HomeWindow();
            WindowUtils.register(instance);
        }
    }
}
