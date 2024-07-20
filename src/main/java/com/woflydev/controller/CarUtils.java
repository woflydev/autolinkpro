package com.woflydev.controller;

import com.woflydev.model.Car;
import com.woflydev.model.Globals;

import java.util.ArrayList;
import java.util.List;

public class CarUtils {
    public static void addCar(Car car) {
        List<Car> cars = FileUtils.loadListFromDisk(Globals.CARS_FILE, Car[].class);
        if (cars == null) {
            cars = new ArrayList<>();
        }
        cars.add(car);
        FileUtils.saveToDisk(cars, Globals.CARS_FILE);
    }

    public static void deleteCar(String carId) {
        List<Car> cars = FileUtils.loadListFromDisk(Globals.CARS_FILE, Car[].class);
        if (cars != null) {
            cars.removeIf(car -> car.getId().equals(carId));
            FileUtils.saveToDisk(cars, Globals.CARS_FILE);
        }
    }
}
