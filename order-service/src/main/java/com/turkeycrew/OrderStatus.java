package com.turkeycrew;

public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Optionally, you can add a method to convert a String to OrderStatus
    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus value: " + value);
    }
}
