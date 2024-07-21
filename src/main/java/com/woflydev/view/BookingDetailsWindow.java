package com.woflydev.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.woflydev.controller.BookingUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Booking;
import com.woflydev.model.Car;
import com.woflydev.model.Globals;
import com.woflydev.model.User;
import com.woflydev.model.enums.PaymentMethod;
import com.woflydev.controller.CarUtils; // Make sure to import CarUtils

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class BookingDetailsWindow extends JFrame implements ActionListener {
    public static BookingDetailsWindow instance = null;

    private JTextField customerNameField;
    private JTextField customerEmailField;
    private DatePicker startDatePicker;
    private TimePicker startTimePicker;
    private DatePicker endDatePicker;
    private TimePicker endTimePicker;
    private JButton confirmButton;
    private JComboBox<PaymentMethod> paymentMethodComboBox; // Updated to use PaymentMethod enum
    private String carId;

    public BookingDetailsWindow(String carId) {
        this.carId = carId;

        User c = UserUtils.getUserByEmail(Globals.CURRENT_USER_EMAIL);
        String customerName = "";
        String customerEmail = "";
        if (c != null) {
            customerName = c.getFullName();
            customerEmail = c.getEmail();
        }

        setTitle("Booking Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel for input fields and button
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Enter Booking Details", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Allow the heading to occupy some vertical space
        mainPanel.add(headingLabel, gbc);

        // Customer Name field
        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField(20);
        customerNameField.setText(customerName);
        gbc.gridwidth = 1; // Reset gridwidth for input fields
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(customerNameLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(customerNameField, gbc);

        // Customer Email field
        JLabel customerEmailLabel = new JLabel("Customer Email:");
        customerEmailField = new JTextField(20);
        customerEmailField.setText(customerEmail);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(customerEmailLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(customerEmailField, gbc);

        // Start Date picker
        JLabel startDateLabel = new JLabel("Start Date:");
        startDatePicker = new DatePicker();
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(startDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startDatePicker, gbc);

        // Start Time picker
        JLabel startTimeLabel = new JLabel("Start Time:");
        startTimePicker = new TimePicker();
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(startTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startTimePicker, gbc);

        // End Date picker
        JLabel endDateLabel = new JLabel("End Date:");
        endDatePicker = new DatePicker();
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(endDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endDatePicker, gbc);

        // End Time picker
        JLabel endTimeLabel = new JLabel("End Time:");
        endTimePicker = new TimePicker();
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(endTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endTimePicker, gbc);

        // Payment Method selection
        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodComboBox = new JComboBox<>(PaymentMethod.values()); // Use enum values
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(paymentMethodLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(paymentMethodComboBox, gbc);

        // Confirm Button
        confirmButton = new JButton("Confirm Booking");
        confirmButton.setPreferredSize(new Dimension(150, 40));
        confirmButton.setBackground(new Color(0, 120, 215));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Allow the button to occupy some vertical space
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(confirmButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(500, 600); // Adjusted size for new components
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmButton) {
            confirmBooking();
        }
    }

    private void confirmBooking() {
        String customerName = customerNameField.getText();
        String customerEmail = customerEmailField.getText();
        LocalDate startDate = startDatePicker.getDate();
        LocalTime startTime = startTimePicker.getTime();
        LocalDate endDate = endDatePicker.getDate();
        LocalTime endTime = endTimePicker.getTime();
        PaymentMethod paymentMethod = (PaymentMethod) paymentMethodComboBox.getSelectedItem(); // Get selected payment method

        // Debugging: Log values retrieved
        System.out.println("Start Date: " + startDate);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Date: " + endDate);
        System.out.println("End Time: " + endTime);
        System.out.println("Payment Method: " + paymentMethod);

        if (customerName.isEmpty() || customerEmail.isEmpty() || startDate == null || startTime == null || endDate == null || endTime == null || paymentMethod == null) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        if (endDateTime.isBefore(startDateTime)) {
            JOptionPane.showMessageDialog(this, "End date must be after start date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking newBooking = new Booking(UUID.randomUUID().toString(), carId, customerName, customerEmail, startDateTime, endDateTime, paymentMethod);
        BookingUtils.addBooking(newBooking);

        // Open Booking Confirmation Window
        BookingConfirmationWindow confirmationWindow = new BookingConfirmationWindow(newBooking);
        confirmationWindow.setVisible(true);

        dispose();
    }

    public static void open(String carId) {
        if (instance == null) {
            instance = new BookingDetailsWindow(carId);
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
