package com.woflydev.model.enums;

public enum PaymentMethod {
    CREDIT_DEBIT("Credit/Debit"),
    PAYPAL("PayPal"),
    IN_PERSON_CASH("In-Person Cash");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
