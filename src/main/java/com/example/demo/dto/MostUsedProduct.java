package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MostUsedProduct {
    private ProductDto product;
    private Long count;
}
