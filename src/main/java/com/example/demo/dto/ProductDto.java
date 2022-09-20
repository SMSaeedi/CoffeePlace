package com.example.demo.dto;

import com.example.demo.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private int id;
    private String name;
    private ProductType type;
    private BigDecimal price;
}