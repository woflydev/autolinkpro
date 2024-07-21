package com.woflydev.model;

public class Globals {
    public static final String USERS_DIR = "data\\users\\";
    public static final String SYSTEM_DIR = "data\\system\\";

    public static final String CUSTOMERS_FILE = USERS_DIR + "customers.json";
    public static final String OWNER_FILE = USERS_DIR + "owner.json";
    public static final String STAFF_FILE = USERS_DIR + "staff.json";
    public static final String CARS_FILE = SYSTEM_DIR + "cars.json";
    public static final String BOOKINGS_FILE = SYSTEM_DIR + "bookings.json";

    public static final short PRIVILEGE_NONE = -1;
    public static final short PRIVILEGE_CUSTOMER = 2;
    public static final short PRIVILEGE_STAFF = 1;
    public static final short PRIVILEGE_OWNER = 0;

    // runtime globals
    public static short CURRENT_PRIVILEGE = -1;
    public static String CURRENT_USER_EMAIL = "";
}
