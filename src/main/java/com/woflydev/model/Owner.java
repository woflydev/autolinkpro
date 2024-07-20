package com.woflydev.model;

public class Owner extends User {
    public Owner(String firstName,
                 String lastName,
                 String email,
                 String password) {
        super(firstName, lastName, email, password, Globals.PRIVILEGE_OWNER);

        this.privilege = Globals.PRIVILEGE_OWNER;
    }
}
