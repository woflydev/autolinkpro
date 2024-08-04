package com.woflydev.model;

import java.io.File;

/**
 * Static system constants that should not be edited.
 * Runtime globals are set in the controller, and are referred to by its methods.
 * @author woflydev
 */
public class Globals {
    // static globals
    public static final String USERS_DIR = "data" + File.separator + "users" + File.separator;
    public static final String SYSTEM_DIR = "data" + File.separator + "system" + File.separator;

    public static final short PRIVILEGE_NONE = -1;
    public static final short PRIVILEGE_CUSTOMER = 2;
    public static final short PRIVILEGE_STAFF = 1;
    public static final short PRIVILEGE_OWNER = 0;

    // runtime globals
    public static short CURRENT_PRIVILEGE = -1;
    public static String CURRENT_USER_EMAIL = "";
}
