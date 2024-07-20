package com.woflydev.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateBookingWindow extends JFrame implements ActionListener {
    private JTextField customerNameField;
    private JTextField customerEmailField;
    private JTextField bookingDateField;
    private JButton submitButton;
    private JButton cancelButton;

    public CreateBookingWindow() {
        setTitle("Create Booking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel with border
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Create Booking", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Allow the heading to occupy some vertical space
        mainPanel.add(headingLabel, gbc);

        // Customer Name
        JLabel customerNameLabel = new JLabel("Customer Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.0;
        mainPanel.add(customerNameLabel, gbc);

        customerNameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(customerNameField, gbc);

        // Customer Email
        JLabel customerEmailLabel = new JLabel("Customer Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(customerEmailLabel, gbc);

        customerEmailField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(customerEmailField, gbc);

        // Booking Date
        JLabel bookingDateLabel = new JLabel("Booking Date:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(bookingDateLabel, gbc);

        bookingDateField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(bookingDateField, gbc);

        // Submit and Cancel Buttons
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        mainPanel.add(submitButton, gbc);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(cancelButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            // Handle the booking submission logic here
            String name = customerNameField.getText();
            String email = customerEmailField.getText();
            String date = bookingDateField.getText();

            // Example logic: Print to console (you can replace this with actual logic)
            System.out.println("Booking Submitted:");
            System.out.println("Name: " + name);
            System.out.println("Email: " + email);
            System.out.println("Date: " + date);

            // Optionally close the window after submission
            dispose();
        } else if (e.getSource() == cancelButton) {
            // Close the window if the user cancels
            dispose();
        }
    }

    public static void open() {
        CreateBookingWindow bookingWindow = new CreateBookingWindow();
        bookingWindow.setVisible(true);
    }
}
