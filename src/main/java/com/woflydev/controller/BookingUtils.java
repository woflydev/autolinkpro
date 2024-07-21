package com.woflydev.controller;

import com.woflydev.model.Booking;
import com.woflydev.model.Globals;

import java.awt.print.Book;
import java.util.List;

public class BookingUtils {
    public static List<Booking> getBookingList() { return FileUtils.getEntityList(Globals.BOOKINGS_FILE, Booking[].class); }
    public static void addBooking(Booking booking) { FileUtils.addEntity(booking, Globals.BOOKINGS_FILE, Booking[].class); }

    public static void deleteBooking(String id) { FileUtils.deleteEntity(Globals.BOOKINGS_FILE, Booking[].class, id, Booking::getId); }
    public static void deleteBooking(Booking booking) { FileUtils.deleteEntity(Globals.BOOKINGS_FILE, Booking[].class, booking.getId(), Booking::getId); }
}
