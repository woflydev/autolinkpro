package com.woflydev.controller;

import com.woflydev.model.obj.Booking;

import static com.woflydev.model.Globals.*;
import static com.woflydev.model.Config.*;

import java.util.List;


public class BookingUtils {
    public static List<Booking> getBookingList() { return FileUtils.getEntityList(BOOKINGS_FILE, Booking[].class); }
    public static void addBooking(Booking booking) { FileUtils.addEntity(booking, BOOKINGS_FILE, Booking[].class); }

    public static void deleteBooking(String id) { FileUtils.deleteEntity(BOOKINGS_FILE, Booking[].class, id, Booking::getId); }
    public static void deleteBooking(Booking booking) { FileUtils.deleteEntity(BOOKINGS_FILE, Booking[].class, booking.getId(), Booking::getId); }

    public static boolean userCanMakeBooking() {
        short i = 0;
        for (Booking booking : getBookingList()) {
            if (booking.getCustomerEmail().equals(CURRENT_USER_EMAIL)) i++;
            if (i == MAX_CONCURRENT_BOOKINGS) return false;
        } return true;
    }
}
