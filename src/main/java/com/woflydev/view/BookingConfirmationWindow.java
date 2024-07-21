package com.woflydev.view;

import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Booking;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class BookingConfirmationWindow extends JFrame {
    public static BookingConfirmationWindow instance = null;

    public BookingConfirmationWindow(Booking booking) {
        setTitle("Booking Confirmed!");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel headingLabel = new JLabel("Booking Confirmed!", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        JLabel bookingIdLabel = new JLabel("Booking ID: " + booking.getId());
        JLabel carInfoLabel = new JLabel("Car Details: " + booking.getCar().getMake() + " " + booking.getCar().getModel());
        JLabel customerNameLabel = new JLabel("Driver Name: " + booking.getDriverFullName());
        JLabel customerEmailLabel = new JLabel("Driver Email: " + booking.getDriverEmail());
        JLabel startDateTimeLabel = new JLabel("Start Date & Time: " + booking.getStartDateTime().format(dateTimeFormatter));
        JLabel endDateTimeLabel = new JLabel("End Date & Time: " + booking.getEndDateTime().format(dateTimeFormatter));
        JLabel paymentMethodLabel = new JLabel("Payment Method: " + booking.getPaymentMethod());

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(bookingIdLabel, gbc);
        gbc.gridy = 2;
        mainPanel.add(carInfoLabel, gbc);
        gbc.gridy = 3;
        mainPanel.add(customerNameLabel, gbc);
        gbc.gridy = 4;
        mainPanel.add(customerEmailLabel, gbc);
        gbc.gridy = 5;
        mainPanel.add(startDateTimeLabel, gbc);
        gbc.gridy = 6;
        mainPanel.add(endDateTimeLabel, gbc);
        gbc.gridy = 7;
        mainPanel.add(paymentMethodLabel, gbc);

        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 40));
        okButton.setBackground(new Color(0, 120, 215));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dispose());
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Allow the button to occupy some vertical space
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(okButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    public static void open(Booking booking) {
        if (instance == null) {
            instance = new BookingConfirmationWindow(booking);
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
