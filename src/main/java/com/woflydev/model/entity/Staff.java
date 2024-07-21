package com.woflydev.model.entity;

import com.woflydev.model.Globals;

public class Staff extends User {
    public Staff(String firstName,
                 String lastName,
                 String email,
                 String password) {
        super(firstName, lastName, email, password, Globals.PRIVILEGE_STAFF);

        this.privilege = Globals.PRIVILEGE_STAFF;
    }
}
