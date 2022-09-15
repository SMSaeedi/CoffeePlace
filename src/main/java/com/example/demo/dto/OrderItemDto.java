package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private ProductDto product;
    private Integer quantity;
    private BigDecimal amount;
    private BigDecimal discount;
}
