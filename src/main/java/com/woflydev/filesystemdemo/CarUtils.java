package com.woflydev.filesystemdemo;

import com.woflydev.model.obj.Car;

import java.util.List;

import static com.woflydev.filesystemdemo.Config.CARS_FILE;

public class CarUtils {
    public static List<Car> getCarList() { return FileUtils.getEntityList(CARS_FILE, Car[].class); }
    public static void addCar(Car car) { FileUtils.addEntity(car, CARS_FILE, Car[].class); }
    public static void deleteCar(String carId) { FileUtils.deleteEntity(CARS_FILE, Car[].class, carId, Car::getId); }
    public static void updateCar(Car updatedCar) { FileUtils.updateEntity(CARS_FILE, Car[].class, updatedCar, Car::getId); }
    public static Car getCarById(String id) { return FileUtils.getEntityById(CARS_FILE, Car[].class, id, Car::getId); }
}
