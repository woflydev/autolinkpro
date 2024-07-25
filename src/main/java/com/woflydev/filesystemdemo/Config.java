package com.woflydev.filesystemdemo;

import static com.woflydev.filesystemdemo.Globals.SYSTEM_DIR;
import static com.woflydev.filesystemdemo.Globals.USERS_DIR;

public class Config {
    public static final String CUSTOMERS_FILE = USERS_DIR + "customers.json";
    public static final String STAFF_FILE = USERS_DIR + "staff.json";
    public static final String OWNER_FILE = USERS_DIR + "owner.json";

    public static final String CARS_FILE = SYSTEM_DIR + "cars.json";
    public static final String BOOKINGS_FILE = SYSTEM_DIR + "bookings.json";

    public static final short MAX_BOOKING_DAYS = 7;
    public static final short MAX_CONCURRENT_BOOKINGS = 3;
    public static final short TURNOVER_TIME = 60; // in minutes
}
