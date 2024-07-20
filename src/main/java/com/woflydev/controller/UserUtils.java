package com.woflydev.controller;

import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.Customer;
import com.woflydev.model.Owner;
import com.woflydev.model.Staff;
import com.woflydev.model.User;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.woflydev.controller.FileUtils.initializeSystem;
import static com.woflydev.model.Globals.*;

public class UserUtils {
    public static List<User> getUsers() {
        FileUtils.initializeSystem(); // Ensure system is initialized
        List<User> users = FileUtils.loadListFromDisk(USER_FILE, User[].class);
        return users != null ? users : new ArrayList<>();
    }

    public static List<Customer> getCustomers() {
        FileUtils.initializeSystem();
        List<Customer> customers = FileUtils.loadListFromDisk(USER_FILE, Customer[].class);
        return customers != null ? customers : new ArrayList<>();
    }

    public static List<Staff> getStaff() {
        FileUtils.initializeSystem();
        List<Staff> staff = FileUtils.loadListFromDisk(USER_FILE, Staff[].class);
        return staff != null ? staff : new ArrayList<>();
    }

    private static Owner getOwner() {
        FileUtils.initializeSystem();
        List<Owner> owners = FileUtils.loadListFromDisk(OWNER_FILE, Owner[].class);
        return (owners != null && !owners.isEmpty()) ? owners.get(0) : null;
    }

    public static void addUser(User user) {
        List<User> users = getUsers();
        users.add(user);
        FileUtils.saveToDisk(users, USER_FILE);
    }

    public static void deleteUser(String email) {
        List<User> users = getUsers();
        users.removeIf(user -> user.getEmail().equals(email));
        FileUtils.saveToDisk(users, USER_FILE);
    }

    public static @Nullable User findUserByEmail(String email) {
        Owner owner = getOwner();
        if (owner != null && owner.getEmail().equals(email)) {
            return new User(owner.getFirstName(), owner.getLastName(), owner.getEmail(), owner.getPassword(), PRIVILEGE_OWNER);
        }

        List<User> users = getUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    public static boolean authenticate(String email, String password) {
        // Check if the email belongs to the owner
        Owner owner = getOwner();
        if (owner != null && owner.getEmail().equals(email)) {
            return BCryptHash.verifyHash(password, owner.getPassword());
        }

        // Check if the email belongs to a user
        User user = findUserByEmail(email);
        return user != null && BCryptHash.verifyHash(password, user.getPassword());
    }

    public static boolean isOwner(String email) {
        Owner owner = getOwner();
        return owner != null && owner.getEmail().equals(email);
    }

    public static boolean hasPrivilege(String email, short requiredPrivilege) {
        if (isOwner(email)) {
            return true; // Owner has the highest privilege
        }
        User user = findUserByEmail(email);
        return user != null && user.getPrivilege() < requiredPrivilege;
    }

    public static void updateUser(User updatedUser) {
        List<User> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(updatedUser.getEmail())) {
                users.set(i, updatedUser);
                FileUtils.saveToDisk(users, USER_FILE);
                return;
            }
        }

        // If the user to update is the owner, update the owner's details
        Owner owner = getOwner();
        if (owner != null && owner.getEmail().equals(updatedUser.getEmail())) {
            owner.setFirstName(updatedUser.getFirstName());
            owner.setLastName(updatedUser.getLastName());
            owner.setEmail(updatedUser.getEmail());
            owner.setPassword(updatedUser.getPassword());
            List<Owner> owners = new ArrayList<>();
            owners.add(owner);
            FileUtils.saveToDisk(owners, OWNER_FILE);
        }
    }
}
