package com.woflydev.model;

public class Globals {
    public static final String USER_FILE = "users.json";
    public static final String OWNER_FILE = "owner.json";

    public static final short PRIVILEGE_NONE = -1;
    public static final short PRIVILEGE_CUSTOMER = 2;
    public static final short PRIVILEGE_STAFF = 1;
    public static final short PRIVILEGE_OWNER = 0;

    public static short CURRENT_PRIVILEGE = -1;
    public static String CURRENT_USER_EMAIL = ""; // Add this to hold the current user's email
}
