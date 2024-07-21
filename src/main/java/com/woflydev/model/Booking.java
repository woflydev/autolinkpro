package com.woflydev.model;

import com.woflydev.controller.CarUtils;
import com.woflydev.model.enums.PaymentMethod;

import java.time.LocalDateTime;

public class Booking {
    private String id;
    private String carId;
    private String customerEmail;
    private String driverFullName;
    private String driverEmail;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private PaymentMethod paymentMethod;

    public Booking(String id, String carId, String customerEmail, String driverFullName, String driverEmail, LocalDateTime startDateTime, LocalDateTime endDateTime, PaymentMethod paymentMethod) {
        this.id = id;
        this.carId = carId;
        this.customerEmail = customerEmail;
        this.driverFullName = driverFullName;
        this.driverEmail = driverEmail;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Car getCar() {
        return CarUtils.getCarById(carId);
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
