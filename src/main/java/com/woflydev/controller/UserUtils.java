package com.woflydev.controller;

import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.woflydev.model.Globals.*;

public class UserUtils {

    private static <T> List<T> getEntityList(String fileName, Class<T[]> clazz) {
        FileUtils.initializeSystem();
        List<T> entities = FileUtils.loadListFromDisk(fileName, clazz);
        return entities != null ? entities : new ArrayList<>();
    }

    private static <T> void saveEntityList(List<T> entities, String fileName) {
        FileUtils.saveToDisk(entities, fileName);
    }

    private static <T> void addEntity(T entity, String fileName, Class<T[]> clazz) {
        List<T> entities = getEntityList(fileName, clazz);
        entities.add(entity);
        saveEntityList(entities, fileName);
    }

    private static <T> void deleteEntity(String fileName, Class<T[]> clazz, String email, EmailGetter<T> emailGetter) {
        List<T> entities = getEntityList(fileName, clazz);
        entities.removeIf(entity -> emailGetter.getEmail(entity).equals(email));
        saveEntityList(entities, fileName);
    }

    private static <T> T getEntityByEmail(String fileName, Class<T[]> clazz, String email, EmailGetter<T> emailGetter) {
        List<T> entities = getEntityList(fileName, clazz);
        for (T entity : entities) {
            if (emailGetter.getEmail(entity).equals(email)) {
                return entity;
            }
        }
        return null;
    }

    private static <T> void updateEntity(String fileName, Class<T[]> clazz, T updatedEntity, EmailGetter<T> emailGetter) {
        List<T> entities = getEntityList(fileName, clazz);
        for (int i = 0; i < entities.size(); i++) {
            if (emailGetter.getEmail(entities.get(i)).equals(emailGetter.getEmail(updatedEntity))) {
                entities.set(i, updatedEntity);
                saveEntityList(entities, fileName);
                return;
            }
        }
    }

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

    //------------------------------------helper methods--------------------------------------------------\\

    public static boolean hasPrivilege(String email, short requiredPrivilege) {
        if (getOwner() != null && getOwner().getEmail().equals(email))
            return true; // Owner has the highest privilege
        else if (getStaffByEmail(email) != null && getStaffByEmail(email).getEmail().equals(email))
            return getStaffByEmail(email).getPrivilege() < requiredPrivilege;

        Customer customer = getCustomerByEmail(email);
        return customer != null && customer.getPrivilege() < requiredPrivilege;
    }

    public static List<Staff> getStaffList() { return getEntityList(STAFF_FILE, Staff[].class); }
    public static List<Customer> getCustomerList() { return getEntityList(CUSTOMERS_FILE, Customer[].class); }
    public static @Nullable Staff getStaffByEmail(String email) { return getEntityByEmail(STAFF_FILE, Staff[].class, email, Staff::getEmail); }
    public static @Nullable Customer getCustomerByEmail(String email) { return getEntityByEmail(CUSTOMERS_FILE, Customer[].class, email, Customer::getEmail); }

    public static void addStaff(Staff staff) { addEntity(staff, STAFF_FILE, Staff[].class); }
    public static void addCustomer(Customer customer) { addEntity(customer, CUSTOMERS_FILE, Customer[].class); }

    public static void deleteStaff(String email) { deleteEntity(STAFF_FILE, Staff[].class, email, Staff::getEmail); }
    public static void deleteCustomer(String email) { deleteEntity(CUSTOMERS_FILE, Customer[].class, email, Customer::getEmail); }

    public static void updateStaff(Staff updatedStaff) { updateEntity(STAFF_FILE, Staff[].class, updatedStaff, Staff::getEmail); }
    public static void updateCustomer(Customer updatedCustomer) { updateEntity(CUSTOMERS_FILE, Customer[].class, updatedCustomer, Customer::getEmail); }


    public static Owner getOwner() { // shorthand
        List<Owner> owners = FileUtils.loadListFromDisk(OWNER_FILE, Owner[].class);
        return (owners != null && !owners.isEmpty()) ? owners.get(0) : null;
    }

    @FunctionalInterface
    public interface EmailGetter<T> {
        String getEmail(T entity);
    }
}
