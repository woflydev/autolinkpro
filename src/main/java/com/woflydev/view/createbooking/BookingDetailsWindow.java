package com.woflydev.view.createbooking;

import com.woflydev.controller.BookingUtils;
import com.woflydev.controller.StyleUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.view.util.swing.CustomDatePicker;
import com.woflydev.view.util.swing.CustomTimePicker;
import com.woflydev.model.obj.Booking;
import com.woflydev.model.Config;
import com.woflydev.model.entity.User;
import com.woflydev.model.enums.PaymentMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.woflydev.controller.StyleUtils.BUTTON_STYLES.PRIMARY;
import static com.woflydev.model.Globals.CURRENT_USER_EMAIL;

/**
 * This window requests details about the Booking to be created.
 * Additionally, it handles data validation.
 * <p>
 * This window is the parent of {@link com.woflydev.view.manage.EditBookingWindow}
 */
public class BookingDetailsWindow extends JFrame implements ActionListener {
    public static BookingDetailsWindow instance = null;

    protected JTextField driverNameField;
    protected JTextField driverEmailField;
    protected CustomDatePicker startDatePicker;
    protected CustomTimePicker startTimePicker;
    protected CustomDatePicker endDatePicker;
    protected CustomTimePicker endTimePicker;
    protected JComboBox<PaymentMethod> paymentMethodComboBox; // Updated to use PaymentMethod enum
    private String carId;
    private JButton confirmButton;

    protected JLabel headingLabel;

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

        headingLabel = new JLabel("Enter Booking Details", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        JLabel driverNameLabel = new JLabel("Driver Name:");
        driverNameField = new JTextField(20);
        driverNameField.setText(customerName);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(driverNameLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(driverNameField, gbc);

        JLabel driverEmailLabel = new JLabel("Driver Email:");
        driverEmailField = new JTextField(20);
        driverEmailField.setText(customerEmail);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(driverEmailLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(driverEmailField, gbc);

        JLabel startDateLabel = new JLabel("Start Date:");
        startDatePicker = new CustomDatePicker();
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(startDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startDatePicker.createParent(), gbc);

        JLabel startTimeLabel = new JLabel("Start Time:");
        startTimePicker = new CustomTimePicker();
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(startTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(startTimePicker.createParent(), gbc);

        JLabel endDateLabel = new JLabel("End Date:");
        endDatePicker = new CustomDatePicker() ;
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(endDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endDatePicker.createParent(), gbc);

        JLabel endTimeLabel = new JLabel("End Time:");
        endTimePicker = new CustomTimePicker();
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(endTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(endTimePicker.createParent(), gbc);

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
        StyleUtils.applyButtonStyle(confirmButton, PRIMARY);
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

    String driverName;
    String driverEmail;
    LocalDate startDate;
    LocalTime startTime;
    LocalDate endDate;
    LocalTime endTime;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    PaymentMethod paymentMethod;
    private void confirmBooking() {
        driverName = driverNameField.getText();
        driverEmail = driverEmailField.getText();
        startDate = startDatePicker.getDate();
        startTime = startTimePicker.getTime();
        endDate = endDatePicker.getDate();
        endTime = endTimePicker.getTime();
        paymentMethod = (PaymentMethod) paymentMethodComboBox.getSelectedItem();

        if (BookingUtils.hasExceededMaximumBookings()) { WindowUtils.errorBox("You can only make a maximum of " + Config.MAX_CONCURRENT_BOOKINGS + " concurrent bookings."); return; }
        if (driverName.isEmpty() || driverEmail.isEmpty() || startDate == null || startTime == null || endDate == null || endTime == null || paymentMethod == null) {
            WindowUtils.errorBox("All fields must be filled out.");
            return;
        }

        startDateTime = LocalDateTime.of(startDate, startTime);
        endDateTime = LocalDateTime.of(endDate, endTime);
        LocalDateTime minThresh = LocalDateTime.now().plusHours(1);

        if (endDateTime.isBefore(startDateTime)) { WindowUtils.errorBox("You can't rent a car for negative time!"); return; }
        if (startDateTime.isBefore(minThresh)) { WindowUtils.errorBox("Start time must be at least an hour after now."); return; }
        if (BookingUtils.exceedsMaximumTime(startDateTime, endDateTime)) { WindowUtils.errorBox("The maximum booking period is " + Config.MAX_BOOKING_DAYS + " days."); return; }

        if (checkHasClash(startDateTime, endDateTime)) {
            LocalDateTime next = BookingUtils.nextAvailable(carId, startDateTime);
            assert next != null;
            String date = next.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = next.format(DateTimeFormatter.ofPattern("HH:mm"));
            String errorMessage = String.format(
                    """
                            This car is already booked during the selected time period.
                            The next available start time given your selected length is %s, at %s."""
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

        processBooking(newBooking);
        BookingConfirmationWindow.open(newBooking);
        dispose();
    }

    // overridden in EditBookingWindow for updating the booking instead of creating a new one
    protected void processBooking(Booking newBooking) {
        BookingUtils.addBooking(newBooking);
    }

    // also overridden
    protected boolean checkHasClash(LocalDateTime start, LocalDateTime end) {
        return BookingUtils.hasClash(carId, "0", start, end);
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
