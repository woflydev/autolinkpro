package com.woflydev.model;

import static com.woflydev.model.Globals.*;

public class Config {
    public static final String CUSTOMERS_FILE = USERS_DIR + "customers.json";
    public static final String STAFF_FILE = USERS_DIR + "staff.json";
    public static final String OWNER_FILE = USERS_DIR + "owner.json";

    public static final String CARS_FILE = SYSTEM_DIR + "cars.json";
    public static final String BOOKINGS_FILE = SYSTEM_DIR + "bookings.json";

    public static final short MAX_CONCURRENT_BOOKINGS = 3;
}
