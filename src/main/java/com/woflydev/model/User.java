package com.woflydev.model;

public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;

    protected short PRIVILEGE = Globals.PRIVILEGE_NONE;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
