package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartDto {
    private BigDecimal totalPrice;
    private String description;
    private List<CartItemDto> items;
}