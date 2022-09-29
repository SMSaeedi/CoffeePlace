package com.example.demo.dto;

import com.example.demo.dao.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostUsedToppings {
    private Product product;
    private Long count;
}
