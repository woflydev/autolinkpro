package com.woflydev.model;

public class Staff extends User {
    public Staff(String firstName,
                 String lastName,
                 String email,
                 String password) {
        super(firstName, lastName, email, password, Globals.PRIVILEGE_STAFF);

        this.privilege = Globals.PRIVILEGE_STAFF;
    }
}
