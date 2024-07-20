package com.woflydev.controller;

import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.Owner;
import com.woflydev.model.User;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.woflydev.model.Globals.*;

public class UserUtils {

    public static void initializeSystem() {
        // Ensure the owner file exists and is initialized
        File ownerFile = new File(OWNER_FILE);
        if (!ownerFile.exists()) {
            System.out.println("Initializing owner file...");
            Owner owner = new Owner(
                    "Admin",
                    "User",
                    "admin@autolinkpro.com",
                    BCryptHash.hashString("admin")
            );

            ArrayList<Owner> owners = new ArrayList<>();
            owners.add(owner);
            FileUtils.saveToDisk(owners, OWNER_FILE);
        }

        // Ensure the users file exists and is initialized
        File usersFile = new File(USER_FILE);
        if (!usersFile.exists()) {
            System.out.println("Initializing users file...");
            List<User> users = new ArrayList<>();
            FileUtils.saveToDisk(users, USER_FILE);
        }
    }

    private static List<User> getUsers() {
        initializeSystem(); // Ensure system is initialized
        FileUtils.createBlank(USER_FILE);
        List<User> users = FileUtils.loadListFromDisk(USER_FILE, User[].class);
        return users != null ? users : new ArrayList<>();
    }

    private static Owner getOwner() {
        FileUtils.createBlank(OWNER_FILE);
        List<Owner> owners = FileUtils.loadListFromDisk(OWNER_FILE, Owner[].class);
        return (owners != null && !owners.isEmpty()) ? owners.get(0) : null;
    }

    public static void addUser(User user) {
        List<User> users = getUsers();
        users.add(user);
        FileUtils.saveToDisk(users, USER_FILE);
    }

    public static @Nullable User findUserByEmail(String email) {
        // First, check if the email belongs to the owner
        Owner owner = getOwner();
        if (owner != null && owner.getEmail().equals(email)) {
            return new User(owner.getFirstName(), owner.getLastName(), owner.getEmail(), owner.getPassword(), PRIVILEGE_OWNER);
        }

        // If not the owner, check in the users list
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
        return user != null && user.getPrivilege() <= requiredPrivilege;
    }
}
