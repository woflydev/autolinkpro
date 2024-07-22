package com.woflydev.view.manage;

import com.woflydev.controller.BookingUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.obj.Booking;
import com.woflydev.view.createbooking.BookingDetailsWindow;
import com.woflydev.view.util.swing.CustomDatePicker;
import com.woflydev.view.util.swing.CustomTimePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class EditBookingWindow extends BookingDetailsWindow {
    public static EditBookingWindow instance = null;

    private Booking b;

    public EditBookingWindow(String bookingId) {
        super(
                BookingUtils.getBookingById(bookingId).getCarId()
        );

        headingLabel.setText("Edit Booking Details");
        setTitle("Edit Booking");

        b = BookingUtils.getBookingById(bookingId);
        driverNameField.setText(b.getDriverFullName());
        driverEmailField.setText(b.getDriverEmail());
        startDatePicker.setSelectedDate(b.getStart().toLocalDate());
        startTimePicker.setSelectedTime(b.getStart().toLocalTime());
        endDatePicker.setSelectedDate(b.getEnd().toLocalDate());
        endTimePicker.setSelectedTime(b.getEnd().toLocalTime());
        paymentMethodComboBox.setSelectedItem(b.getPaymentMethod());
    }

    @Override
    protected void processBooking(Booking newBooking) {
        newBooking.setId(b.getId()); // VERY IMPORTANT! the edited booking's ID must be the same as the original
        BookingUtils.updateBooking(newBooking);
        ManageBookingWindow.instance.updateTable();
    }

    @Override
    protected boolean checkHasClash(LocalDateTime start, LocalDateTime end) {
        return BookingUtils.hasClash(b.getCarId(), b.getId(), start, end);
    }

    public static void open(String bookingId) {
        if (instance == null) {
            instance = new EditBookingWindow(bookingId);
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
