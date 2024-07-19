package com.woflydev.model;

public class Customer extends User {
    private String license;

    public Customer(String firstName,
                    String lastName,
                    String email,
                    String password,
                    String license) {
        super(firstName, lastName, email, password);

        this.license = license;
        this.PRIVILEGE = Globals.PRIVILEGE_CUSTOMER;
    }
}
