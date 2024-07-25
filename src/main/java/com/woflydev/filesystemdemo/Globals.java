package com.woflydev.filesystemdemo;

import java.io.File;

public class Globals {
    public static final String VERSION = "0.0.1";

    // static globals
    /**
     * The directory where data is stored.
     * Defaults to "data/system" for cars and bookings, and "data/users" for users.
     */
    public static final String USERS_DIR = "data" + File.separator + "users" + File.separator;
    public static final String SYSTEM_DIR = "data" + File.separator + "system" + File.separator;
}
