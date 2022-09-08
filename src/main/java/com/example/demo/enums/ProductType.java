package com.example.demo.enums;

public enum ProductType {
    COFFEE(0, "COFFEE"),
    TOPPINGS(1, "TOPPINGS");

    private final int code;
    private final String name;

    ProductType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}