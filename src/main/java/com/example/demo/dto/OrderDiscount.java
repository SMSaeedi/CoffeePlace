package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDiscount {
    private BigDecimal totalAmount;
    private String description;
}