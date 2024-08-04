package com.woflydev.model;

import static com.woflydev.model.Globals.*;

/**
 * Configurable global constants for the application, including where data is stored.
 * @author woflydev
 */
public class Config {
    public static final String CUSTOMERS_FILE = USERS_DIR + "customers.json";
    public static final String STAFF_FILE = USERS_DIR + "staff.json";
    public static final String OWNER_FILE = USERS_DIR + "owner.json";

    public static final String CARS_FILE = SYSTEM_DIR + "cars.json";
    public static final String BOOKINGS_FILE = SYSTEM_DIR + "bookings.json";

    // IMPORTANT! This is only used on first startup, or when the owner.json file has to be regenerated.
    public static final String DEFAULT_ADMIN_USERNAME = "admin@autolinkpro.com";
    public static final String DEFAULT_ADMIN_PASSWORD = "changeme";

    public static final short MAX_BOOKING_DAYS = 7;
    public static final short MAX_CONCURRENT_BOOKINGS = 3;
    public static final short TURNOVER_TIME = 60; // in minutes
}
