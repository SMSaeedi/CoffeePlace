package com.example.demo.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private ProductDto product;
    private Integer quantity;
}