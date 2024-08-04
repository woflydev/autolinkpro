package com.woflydev.model.obj;

/**
 * Contains attributes for any given Car in inventory.
 */
public class Car {
    private String id;
    private String make;
    private String model;

    public Car(String id, String make, String model) {
        this.id = id;
        this.make = make;
        this.model = model;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
