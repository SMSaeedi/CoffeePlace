package com.example.demo.dto;

import lombok.Data;

@Data
public class MostUsedProduct {
    private ProductDto product;
    private Long count;
}
