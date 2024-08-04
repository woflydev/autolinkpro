package com.woflydev.controller;

import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.entity.Customer;
import com.woflydev.model.entity.Owner;
import com.woflydev.model.entity.Staff;
import com.woflydev.model.entity.User;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.woflydev.controller.FileUtils.*;
import static com.woflydev.model.Config.*;
import static com.woflydev.model.Globals.*;

public class UserUtils {
    public static boolean authenticate(String email, String password) {
        Owner owner = getOwner();
        Staff staff = getStaffByEmail(email);

        if (owner != null && owner.getEmail().equals(email))
            return BCryptHash.verifyHash(password, owner.getPassword());
        else if (staff != null && staff.getEmail().equals(email))
            return BCryptHash.verifyHash(password, staff.getPassword());

        Customer customer = getCustomerByEmail(email);
        return customer != null && BCryptHash.verifyHash(password, customer.getPassword());
    }

    public static void updatePassword(String email, String newPassword) {
        Owner owner = getOwner();
        if (owner != null && owner.getEmail().equals(email)) {
            owner.setPassword(BCryptHash.hashString(newPassword));
            updateOwner(owner);
            return;
        }

        Staff staff = getStaffByEmail(email);
        if (staff != null) {
            staff.setPassword(BCryptHash.hashString(newPassword));
            updateStaff(staff);
            return;
        }

        Customer customer = getCustomerByEmail(email);
        if (customer != null) {
            customer.setPassword(BCryptHash.hashString(newPassword));
            updateCustomer(customer);
        }
    }


    public static boolean hasPrivilege(String email, short requiredPrivilege) {
        if (getOwner() != null && getOwner().getEmail().equals(email))
            return true; // owner has the highest privilege
        else if (getStaffByEmail(email) != null && getStaffByEmail(email).getEmail().equals(email))
            return getStaffByEmail(email).getPrivilege() <= requiredPrivilege;

        Customer customer = getCustomerByEmail(email);
        return customer != null && customer.getPrivilege() <= requiredPrivilege;
    }

    public static List<Staff> getStaffList() { return getEntityList(STAFF_FILE, Staff[].class); }
    public static List<Customer> getCustomerList() { return getEntityList(CUSTOMERS_FILE, Customer[].class); }
    public static @Nullable User getUserByEmail(String email) { return getAllEntitiesByEmail(USERS_DIR, User[].class, email, User::getEmail); }
    public static @Nullable Staff getStaffByEmail(String email) { return getEntityByEmail(STAFF_FILE, Staff[].class, email, Staff::getEmail); }
    public static @Nullable Customer getCustomerByEmail(String email) { return getEntityByEmail(CUSTOMERS_FILE, Customer[].class, email, Customer::getEmail); }

    public static void addStaff(Staff staff) { addEntity(staff, STAFF_FILE, Staff[].class); }
    public static void addCustomer(Customer customer) { addEntity(customer, CUSTOMERS_FILE, Customer[].class); }

    public static void deleteStaff(String email) { deleteEntity(STAFF_FILE, Staff[].class, email, Staff::getEmail); }
    public static void deleteCustomer(String email) { deleteEntity(CUSTOMERS_FILE, Customer[].class, email, Customer::getEmail); }

    public static void updateOwner(Owner owner) { updateEntity(OWNER_FILE, Owner[].class, owner, Owner::getEmail); }
    public static void updateStaff(Staff updatedStaff) { updateEntity(STAFF_FILE, Staff[].class, updatedStaff, Staff::getEmail); }
    public static void updateCustomer(Customer updatedCustomer) { updateEntity(CUSTOMERS_FILE, Customer[].class, updatedCustomer, Customer::getEmail); }

    public static Owner getOwner() { // shorthand
        List<Owner> owners = FileUtils.loadListFromDisk(OWNER_FILE, Owner[].class);
        return (owners != null && !owners.isEmpty()) ? owners.get(0) : null;
    }

    public static boolean validateRegistration(String firstName, String lastName, String email, String password, String license, LocalDateTime dob) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || license.isEmpty()) { WindowUtils.errorBox("All fields must be filled out."); return false; }
        if (!isValidEmail(email)) { WindowUtils.errorBox("Invalid email format."); return false; }
        if (getUserByEmail(email) != null) { WindowUtils.errorBox("An account with this email already exists."); return false; }
        if (password.length() < 6) { WindowUtils.errorBox("Password must be at least 6 characters long."); return false; }
        if (license.length() > 11 || license.length() < 10 || !license.matches("^[0-9]+$")) { WindowUtils.errorBox("Invalid license number."); return false; }
        if (dob.isAfter(LocalDateTime.now().minusYears(18))) { WindowUtils.errorBox("You must be at least 18 years old to register."); return false; }

        return true;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern
                .compile(emailRegex)
                .matcher(email)
                .matches();
    }
}
