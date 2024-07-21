package com.woflydev.model;

public class Globals {
    // static globals
    public static final String USERS_DIR = "data\\users\\";
    public static final String SYSTEM_DIR = "data\\system\\";

    public static final short PRIVILEGE_NONE = -1;
    public static final short PRIVILEGE_CUSTOMER = 2;
    public static final short PRIVILEGE_STAFF = 1;
    public static final short PRIVILEGE_OWNER = 0;

    // runtime globals
    public static short CURRENT_PRIVILEGE = -1;
    public static String CURRENT_USER_EMAIL = "";
}
