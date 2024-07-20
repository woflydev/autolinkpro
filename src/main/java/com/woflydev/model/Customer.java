package com.woflydev.model;

import java.time.LocalDateTime;

public class Customer extends User {
    private String license;
    private LocalDateTime dob;

    public Customer(String firstName,
                    String lastName,
                    String email,
                    String password,
                    String license,
                    LocalDateTime dob) {
        super(firstName, lastName, email, password, Globals.PRIVILEGE_CUSTOMER);

        this.license = license;
        this.dob = dob;
    }
}
