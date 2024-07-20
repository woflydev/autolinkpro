package com.woflydev.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.Car;
import com.woflydev.model.Owner;
import com.woflydev.model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.woflydev.model.Globals.OWNER_FILE;
import static com.woflydev.model.Globals.USER_FILE;

public class FileUtils {
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
            saveToDisk(owners, OWNER_FILE);
        }

        // Ensure the users file exists and is initialized
        File usersFile = new File(USER_FILE);
        if (!usersFile.exists()) {
            System.out.println("Initializing users file...");
            List<User> users = new ArrayList<>();
            saveToDisk(users, USER_FILE);
        }

        // Ensure the cars file exists and is initialized
        File carsFile = new File("cars.json");
        if (!carsFile.exists()) {
            System.out.println("Initializing cars file...");
            List<Car> cars = new ArrayList<>();
            saveToDisk(cars, "cars.json");
        }
    }

    public static void saveToDisk(Object data, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            Gson gson = new Gson();
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T loadFromDisk(String filename, Class<T> clazz) {
        try (Reader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, clazz);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> loadListFromDisk(String filename, Class<T[]> clazz) {
        try (Reader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type listType = TypeToken.getParameterized(List.class, clazz.getComponentType()).getType();
            return gson.fromJson(reader, listType);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
