package com.woflydev.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woflydev.controller.adapter.LocalDateTimeAdapter;
import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.Car;
import com.woflydev.model.Globals;
import com.woflydev.model.Owner;
import com.woflydev.model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.woflydev.model.Globals.*;

public class FileUtils {

    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public static void initializeSystem() {
        File ownerFile = new File(OWNER_FILE);
        if (!ownerFile.exists()) {
            System.out.println("NO OWNER FILE DETECTED!");
            System.out.println(
                    """
                            Reverting to defaults:
                            Email: admin@autolinkpro.com
                            Password: admin
                    """
            );
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

        File staffFile = new File(STAFF_FILE);
        if (!staffFile.exists()) {
            System.out.println("Initializing users file...");
            List<User> users = new ArrayList<>();
            saveToDisk(users, STAFF_FILE);
        }

        File usersFile = new File(CUSTOMERS_FILE);
        if (!usersFile.exists()) {
            System.out.println("Initializing users file...");
            List<User> users = new ArrayList<>();
            saveToDisk(users, CUSTOMERS_FILE);
        }

        File carsFile = new File(Globals.CARS_FILE);
        if (!carsFile.exists()) {
            System.out.println("Initializing cars file...");
            List<Car> cars = new ArrayList<>();
            saveToDisk(cars, "cars.json");
        }
    }

    public static void saveToDisk(Object data, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> loadListFromDisk(String filename, Class<T[]> clazz) {
        try (Reader reader = new FileReader(filename)) {
            Type listType = TypeToken.getParameterized(List.class, clazz.getComponentType()).getType();
            return gson.fromJson(reader, listType);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T loadFromDisk(String filename, Class<T> clazz) {
        try (Reader reader = new FileReader(filename)) {
            return gson.fromJson(reader, clazz);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
