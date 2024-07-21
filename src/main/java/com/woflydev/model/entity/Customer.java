package com.woflydev.model.entity;

import com.woflydev.model.Globals;

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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }
}
