package com.woflydev.controller;

import com.woflydev.model.obj.Booking;

import static com.woflydev.model.Globals.*;
import static com.woflydev.model.Config.*;

import java.time.LocalDateTime;
import java.util.List;

public class BookingUtils {
    public static List<Booking> getBookingList() { return FileUtils.getEntityList(BOOKINGS_FILE, Booking[].class); }
    public static void addBooking(Booking booking) { FileUtils.addEntity(booking, BOOKINGS_FILE, Booking[].class); }

    public static void deleteBooking(String id) { FileUtils.deleteEntity(BOOKINGS_FILE, Booking[].class, id, Booking::getId); }
    public static void deleteBooking(Booking booking) { FileUtils.deleteEntity(BOOKINGS_FILE, Booking[].class, booking.getId(), Booking::getId); }

    public static boolean hasExceededMaximumBookings() {
        short i = 0;
        for (Booking booking : getBookingList()) {
            if (booking.getCustomerEmail().equals(CURRENT_USER_EMAIL)) i++;
            if (i == MAX_CONCURRENT_BOOKINGS) return false;
        } return true;
    }

    public static boolean hasClash(String carId, LocalDateTime newStart, LocalDateTime newEnd) {
        List<Booking> bookings = getBookingList();

        for (Booking booking : bookings) {
            if (booking.getCarId().equals(carId)) {
                LocalDateTime existingStart = booking.getStart();
                LocalDateTime existingEnd = booking.getEnd();
                LocalDateTime adjustedExistingEnd = existingEnd.plusMinutes(TURNOVER_TIME);

                if ((newStart.isBefore(adjustedExistingEnd) && newEnd.isAfter(existingStart))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static LocalDateTime nextAvailable(String carId, LocalDateTime from) {
        List<Booking> bookings = getBookingList();

        LocalDateTime nextAvailableTime = from;

        for (Booking booking : bookings) {
            if (booking.getCarId().equals(carId)) {
                LocalDateTime existingEnd = booking.getEnd().plusMinutes(TURNOVER_TIME);

                if (nextAvailableTime.isBefore(existingEnd)) {
                    nextAvailableTime = existingEnd;
                }
            }
        }

        return nextAvailableTime;
    }
}
