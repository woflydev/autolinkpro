package com.woflydev.controller;

import com.woflydev.model.Config;
import com.woflydev.model.obj.Booking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.woflydev.model.Globals.*;
import static com.woflydev.model.Config.*;

public class BookingUtils {

    public static List<Booking> getBookingList() { return FileUtils.getEntityList(BOOKINGS_FILE, Booking[].class); }
    public static Booking getBookingById(String bookingId) {
        return getBookingList().stream()
                .filter(booking -> booking.getId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public static void addBooking(Booking booking) { FileUtils.addEntity(booking, BOOKINGS_FILE, Booking[].class); }
    public static void updateBooking(Booking updatedBooking) { FileUtils.updateEntity(BOOKINGS_FILE, Booking[].class, updatedBooking, Booking::getId); }

    public static void deleteBooking(String id) { FileUtils.deleteEntity(BOOKINGS_FILE, Booking[].class, id, Booking::getId); }
    public static void deleteBooking(Booking booking) { FileUtils.deleteEntity(BOOKINGS_FILE, Booking[].class, booking.getId(), Booking::getId); }

    // -------------------------------------------------------------------------------- \\

    public static boolean exceedsMaximumTime(LocalDateTime start, LocalDateTime end) {
        return start.until(end, ChronoUnit.DAYS) > MAX_BOOKING_DAYS;
    }

    public static boolean hasExceededMaximumBookings() {
        short count = 0;
        for (Booking booking : getBookingList()) {
            if (booking.getCustomerEmail().equals(CURRENT_USER_EMAIL)) {
                count++;
                if (count == MAX_CONCURRENT_BOOKINGS) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasClash(String carId, String bookingId, LocalDateTime newStart, LocalDateTime newEnd) {
        return getClashBookingId(carId, bookingId, newStart, newEnd) != null;
    }

    public static LocalDateTime nextAvailable(String carId, LocalDateTime newStart, LocalDateTime newEnd) {
        List<Booking> bookings = getBookingList();

        LocalDateTime nextAvailableTime = newStart;

        //String clashBookingId = getClashBookingId(carId, null, newStart, newEnd);
        //if (clashBookingId != null) nextAvailableTime = getBookingById(clashBookingId).getEnd();

        for (Booking booking : bookings) {
            if (booking.getCarId().equals(carId)) {
                LocalDateTime existingEnd = booking.getEnd().plusMinutes(TURNOVER_TIME);

                if (nextAvailableTime.isBefore(existingEnd)) nextAvailableTime = existingEnd;
            }
        }

        return nextAvailableTime;
    }

    // this is also an indirect way to check if there's a clash
    // will return null if no clash
    private static String getClashBookingId(String carId, String bookingId, LocalDateTime newStart, LocalDateTime newEnd) {
        List<Booking> bookings = getBookingList();

        for (Booking booking : bookings) {
            if (booking.getCarId().equals(carId) && !booking.getId().equals(bookingId)) {
                LocalDateTime existingStart = booking.getStart();
                LocalDateTime existingEnd = booking.getEnd();
                LocalDateTime adjustedExistingStart = existingStart.minusMinutes(TURNOVER_TIME);
                LocalDateTime adjustedExistingEnd = existingEnd.plusMinutes(TURNOVER_TIME);

                // if any of the new time overlaps with an existing booking,
                // then there is a clash
                if (newStart.isBefore(adjustedExistingEnd) && newEnd.isAfter(adjustedExistingStart)) return booking.getId();
            }
        }

        return null;
    }
}
