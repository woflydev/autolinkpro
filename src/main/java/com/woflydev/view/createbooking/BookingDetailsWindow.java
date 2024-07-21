package com.woflydev.view.createbooking;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.woflydev.controller.BookingUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.view.util.component.CustomDatePicker;
import com.woflydev.view.util.component.CustomTimePicker;
import com.woflydev.model.obj.Booking;
import com.woflydev.model.Config;
import com.woflydev.model.entity.User;
import com.woflydev.model.enums.PaymentMethod;
import raven.datetime.component.date.DatePicker;
import raven.datetime.component.time.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.UUID;

import static com.woflydev.model.Globals.CURRENT_USER_EMAIL;

public class BookingDetailsWindow extends JFrame implements ActionListener {
    public static BookingDetailsWindow instance = null;

    private JTextField driverNameField;
    private JTextField driverEmailField;
    private CustomDatePicker startDatePicker;
    private CustomTimePicker startTimePicker;
    private CustomDatePicker endDatePicker;
    private CustomTimePicker endTimePicker;
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Enter Booking Details", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        JLabel customerNameLabel = new JLabel("Customer Name:");
        driverNameField = new JTextField(20);
        driverNameField.setText(customerName);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(customerNameLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(driverNameField, gbc);

        JLabel customerEmailLabel = new JLabel("Customer Email:");
        driverEmailField = new JTextField(20);
        driverEmailField.setText(customerEmail);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(customerEmailLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(driverEmailField, gbc);

        JLabel startDateLabel = new JLabel("Start Date:");
        startDatePicker = new CustomDatePicker();
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(startDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startDatePicker.create(), gbc);

        JLabel startTimeLabel = new JLabel("Start Time:");
        startTimePicker = new CustomTimePicker();
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(startTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startTimePicker.create(), gbc);

        JLabel endDateLabel = new JLabel("End Date:");
        endDatePicker = new CustomDatePicker() ;
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(endDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endDatePicker.create(), gbc);

        JLabel endTimeLabel = new JLabel("End Time:");
        endTimePicker = new CustomTimePicker();
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(endTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endTimePicker.create(), gbc);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodComboBox = new JComboBox<>(PaymentMethod.values()); // Use enum values
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(paymentMethodLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(paymentMethodComboBox, gbc);

        confirmButton = new JButton("Confirm Booking");
        confirmButton.setPreferredSize(new Dimension(150, 40));
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(confirmButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(500, 600);
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

        if (!BookingUtils.hasExceededMaximumBookings()) {
            WindowUtils.errorBox("You can only make a maximum of " + Config.MAX_CONCURRENT_BOOKINGS + " concurrent bookings.");
            return;
        }
        if (driverName.isEmpty() || driverEmail.isEmpty() || startDate == null || startTime == null || endDate == null || endTime == null || paymentMethod == null) {
            WindowUtils.errorBox("All fields must be filled out.");
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        LocalDateTime minThresh = LocalDateTime.now().plusHours(1);

        if (endDateTime.isBefore(startDateTime)) {
            WindowUtils.errorBox("You can't rent a car for negative time!");
            return;
        }
        if (startDateTime.isBefore(minThresh)) {
            WindowUtils.errorBox("Start time must be at least an hour after now.");
            return;
        }

        if (BookingUtils.hasClash(carId, startDateTime, endDateTime)) {
            LocalDateTime next = BookingUtils.nextAvailable(carId, endDateTime);
            String date = next.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = next.format(DateTimeFormatter.ofPattern("HH:mm"));
            String errorMessage = String.format(
                    """
                            This car is already booked during the selected time period.
                            The next available time is: %s, at %s."""
                    , date, time
            );
            WindowUtils.errorBox(errorMessage);
            return;
        }

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
