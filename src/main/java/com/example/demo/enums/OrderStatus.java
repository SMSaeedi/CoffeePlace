package com.example.demo.enums;

public enum OrderStatus {
    REGISTERED(0, "REGISTERED"),
    ON_THE_WAY(1, "ON_THE_WAY"),
    DELIVERED(2, "DELIVERED"),
    CANCELED(3, "CANCELED");

    private final int code;
    private final String name;

    OrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}