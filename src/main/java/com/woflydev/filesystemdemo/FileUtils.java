package com.woflydev.filesystemdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woflydev.controller.adapter.LocalDateTimeAdapter;
import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.obj.Car;
import com.woflydev.model.entity.Owner;
import com.woflydev.model.entity.User;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.woflydev.filesystemdemo.Config.*;

/**
 * A class that provides generic utility methods for working with files and JSON data.
 * Requires Google's Gson library.
 * Install under package name "com.google.code.gson".
 * <p>
 * If you have Maven, add the dependency to your pom.xml.
 * @author @woflydev
 */
public class FileUtils {

    private static final Gson gson;

    static {
        System.out.println("AutolinkFiler v" + Globals.VERSION);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    /**
     * Should be called in your main method.
     * Creates all the necessary files on launch and/or make sure they exist.
     */
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

        File carsFile = new File(CARS_FILE);
        if (!carsFile.exists()) {
            System.out.println("Initializing cars file...");
            List<Car> cars = new ArrayList<>();
            saveToDisk(cars, CARS_FILE);
        }
    }

    /**
     * Utility method, this should not be called outside of this class.
     * @param data
     * @param filename
     */
    private static void saveToDisk(Object data, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Another utility method, this should not be called outside of this class.
     * @param filename
     * @param clazz
     * @return
     * @param <T>
     */
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

    // entity interaction with filesystem ---------------------------------------------- \\

    /**
     * Returns a list of the specified entity, as dictated by fileName and clazz.
     * @param fileName
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> List<T> getEntityList(String fileName, Class<T[]> clazz) {
        initializeSystem();
        List<T> entities = loadListFromDisk(fileName, clazz);
        return entities != null ? entities : new ArrayList<>();
    }

    /**
     * Saves a list of entities to a specified file on disk.
     * @param entities
     * @param fileName
     * @param <T>
     */
    public static <T> void saveEntityList(List<T> entities, String fileName) {
        saveToDisk(entities, fileName);
    }

    /**
     * Adds a generic entity to a specified file on disk.
     * @param entity
     * @param fileName
     * @param clazz
     * @param <T>
     */
    public static <T> void addEntity(T entity, String fileName, Class<T[]> clazz) {
        List<T> entities = getEntityList(fileName, clazz);
        entities.add(entity);
        saveEntityList(entities, fileName);
    }

    /**
     * Removes a generic entity from the saved file if its ID is found.
     * @param fileName
     * @param clazz
     * @param id
     * @param idGetter
     * @param <T>
     */
    public static <T> void deleteEntity(String fileName, Class<T[]> clazz, String id, IdGetter<T> idGetter) {
        List<T> entities = getEntityList(fileName, clazz);
        entities.removeIf(entity -> idGetter.getId(entity).equals(id));
        saveEntityList(entities, fileName);
    }

    /**
     * Same as getAllEntitiesByEmail (below), but instead, only targets one saved file (specified by fileName)
     * @param fileName
     * @param clazz
     * @param email
     * @param emailGetter
     * @return
     * @param <T>
     */
    public static <T> T getEntityByEmail(
            String fileName,
            Class<T[]> clazz,
            String email,
            EmailGetter<T> emailGetter
    ) {
        List<T> entities = getEntityList(fileName, clazz);
        for (T entity : entities) {
            if (emailGetter.getEmail(entity).equals(email)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * General utility method to get an entity from all JSON files in a directory. For example,
     * best used when you have "customers", "staff", and "owner" all in one directory and want to search
     * for all of them.
     * @param directoryPath
     * @param clazz
     * @param email
     * @param emailGetter
     * @return
     */
    public static User getAllEntitiesByEmail(
            String directoryPath, // Path to the directory containing JSON files
            Class<User[]> clazz, // Class type for the array of Users
            String email, // Email to search for
            EmailGetter<User> emailGetter // Function to extract email from User
    ) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Provided path is not a directory");
        }

        File[] jsonFiles = dir.listFiles((dir1, name) -> name.endsWith(".json"));

        if (jsonFiles != null) {
            for (File file : jsonFiles) {
                List<User> users = getEntityList(file.getAbsolutePath(), clazz);
                for (User user : users) {
                    if (emailGetter.getEmail(user).equals(email)) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Mostly used for Bookings and Cars, since Users (and its extensions) have emails that act as unique identifiers.
     * See BookingUtils and CarUtils for usage details.
     * @param fileName
     * @param clazz
     * @param id
     * @param idGetter
     * @return
     * @param <T>
     */
    public static <T> T getEntityById(
            String fileName,
            Class<T[]> clazz,
            String id,
            IdGetter<T> idGetter
    ) {
        List<T> entities = getEntityList(fileName, clazz);
        for (T entity : entities) {
            if (idGetter.getId(entity).equals(id)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Takes in a fileName to search for, an updatedEntity with their data changed in some way,
     * and then inserts it if an identical ID is found. If the entity doesn't exist, it doesn't do anything.
     * @param fileName
     * @param clazz
     * @param updatedEntity
     * @param idGetter
     * @param <T>
     */
    public static <T> void updateEntity(String fileName, Class<T[]> clazz, T updatedEntity, IdGetter<T> idGetter) {
        List<T> entities = getEntityList(fileName, clazz);
        for (int i = 0; i < entities.size(); i++) {
            if (idGetter.getId(entities.get(i)).equals(idGetter.getId(updatedEntity))) {
                entities.set(i, updatedEntity);
                saveEntityList(entities, fileName);
                return;
            }
        }
    }

    /**
     * Don't worry about these iFaces.
     * @param <T>
     */
    @FunctionalInterface
    public interface EmailGetter<T> {
        String getEmail(T entity);
    }

    @FunctionalInterface
    public interface IdGetter<T> {
        String getId(T entity);
    }
}
