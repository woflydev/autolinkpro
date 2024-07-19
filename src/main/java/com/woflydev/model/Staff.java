package com.woflydev.model;

public class Staff extends User {
    public Staff(String firstName,
                 String lastName,
                 String email,
                 String password) {
        super(firstName, lastName, email, password);

        this.PRIVILEGE = Globals.PRIVILEGE_STAFF;
    }
}
