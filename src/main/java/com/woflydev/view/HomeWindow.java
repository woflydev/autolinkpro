package com.woflydev.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeWindow extends JFrame implements ActionListener {
    public static HomeWindow instance = null;

    private JButton createBookingButton;
    private JButton manageBookingsButton;
    private JButton settingsButton;
    private JButton adminButton;

    public HomeWindow() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2, 10, 10));

        createBookingButton = new JButton("Create Booking");
        manageBookingsButton = new JButton("Manage Bookings");
        settingsButton = new JButton("Settings");
        adminButton = new JButton("Admin");

        createBookingButton.addActionListener(this);
        manageBookingsButton.addActionListener(this);
        settingsButton.addActionListener(this);
        adminButton.addActionListener(this);

        add(createBookingButton);
        add(manageBookingsButton);
        add(settingsButton);
        add(adminButton);

        setSize(400, 300);

        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createBookingButton) {
            System.out.println("Create Booking button clicked");
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
