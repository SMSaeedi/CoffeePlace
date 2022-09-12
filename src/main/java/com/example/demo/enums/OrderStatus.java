package com.example.demo.enums;

public enum OrderStatus {
    ON_THE_WAY(0, "ON_THE_WAY"),
    DELIVERED(1, "DELIVERED"),
    CANCELED(2, "CANCELED");

    private final int code;
    private final String name;

    OrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}