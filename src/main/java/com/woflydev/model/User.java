package com.woflydev.model;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    protected short privilege;  // Use protected to allow access in subclasses or related classes

    public User(String firstName, String lastName, String email, String password, short privilege) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.privilege = privilege;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public short getPrivilege() {
        return privilege;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrivilege(short privilege) {
        this.privilege = privilege;
    }
}
