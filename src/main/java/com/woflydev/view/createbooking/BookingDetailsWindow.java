package com.woflydev.view.createbooking;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.woflydev.controller.BookingUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.view.util.component.CustomDatePicker;
import com.woflydev.view.util.component.CustomTimePicker;
import com.woflydev.model.obj.Booking;
import com.woflydev.model.Config;
import com.woflydev.model.entity.User;
import com.woflydev.model.enums.PaymentMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static com.woflydev.model.Globals.CURRENT_USER_EMAIL;

public class BookingDetailsWindow extends JFrame implements ActionListener {
    public static BookingDetailsWindow instance = null;

    private JTextField driverNameField;
    private JTextField driverEmailField;
    private DatePicker startDatePicker;
    private TimePicker startTimePicker;
    private DatePicker endDatePicker;
    private TimePicker endTimePicker;
    private JButton confirmButton;
    private JComboBox<PaymentMethod> paymentMethodComboBox; // Updated to use PaymentMethod enum
    private String carId;

    public BookingDetailsWindow(String carId) {
        this.carId = carId;

        User c = UserUtils.getUserByEmail(CURRENT_USER_EMAIL);
        String customerName = "";
        String customerEmail = "";
        if (c != null) {
            customerName = c.getFullName();
            customerEmail = c.getEmail();
        }

        setTitle("Booking Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

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
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        // Customer Name field
        JLabel customerNameLabel = new JLabel("Customer Name:");
        driverNameField = new JTextField(20);
        driverNameField.setText(customerName);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(customerNameLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(driverNameField, gbc);

        // Customer Email field
        JLabel customerEmailLabel = new JLabel("Customer Email:");
        driverEmailField = new JTextField(20);
        driverEmailField.setText(customerEmail);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(customerEmailLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(driverEmailField, gbc);

        // Start Date picker
        JLabel startDateLabel = new JLabel("Start Date:");
        startDatePicker = CustomDatePicker.create();
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(startDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startDatePicker, gbc);

        // Start Time picker
        JLabel startTimeLabel = new JLabel("Start Time:");
        startTimePicker = CustomTimePicker.create();
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(startTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startTimePicker, gbc);

        // End Date picker
        JLabel endDateLabel = new JLabel("End Date:");
        endDatePicker = CustomDatePicker.create();
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(endDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endDatePicker, gbc);

        // End Time picker
        JLabel endTimeLabel = new JLabel("End Time:");
        endTimePicker = CustomTimePicker.create();
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(endTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endTimePicker, gbc);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodComboBox = new JComboBox<>(PaymentMethod.values()); // Use enum values
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(paymentMethodLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(paymentMethodComboBox, gbc);

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
        String driverName = driverNameField.getText();
        String driverEmail = driverEmailField.getText();
        LocalDate startDate = startDatePicker.getDate();
        LocalTime startTime = startTimePicker.getTime();
        LocalDate endDate = endDatePicker.getDate();
        LocalTime endTime = endTimePicker.getTime();
        PaymentMethod paymentMethod = (PaymentMethod) paymentMethodComboBox.getSelectedItem();

        if (!BookingUtils.userCanMakeBooking()) { WindowUtils.errorBox("You can only make a maximum of " + Config.MAX_CONCURRENT_BOOKINGS + " concurrent bookings."); return; }
        if (driverName.isEmpty() || driverEmail.isEmpty() || startDate == null || startTime == null || endDate == null || endTime == null || paymentMethod == null) { WindowUtils.errorBox("All fields must be filled out."); return; }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        LocalDateTime minThresh = LocalDateTime.now().plusHours(1);

        if (endDateTime.isBefore(startDateTime)) { WindowUtils.errorBox("You can't rent a car for negative time!"); return; }
        if (startDateTime.isBefore(minThresh)) { WindowUtils.errorBox("Start time must be at least an hour after now."); return; }

        Booking newBooking = new Booking(
                UUID.randomUUID().toString(),
                carId,
                CURRENT_USER_EMAIL,
                driverName,
                driverEmail,
                startDateTime,
                endDateTime,
                paymentMethod
        );

        BookingUtils.addBooking(newBooking);
        BookingConfirmationWindow.open(newBooking);
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
