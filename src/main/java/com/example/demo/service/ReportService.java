package com.example.demo.service;

import com.example.demo.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

public interface ReportService {
    BigDecimal getTotalAmountPerCustomer(int token);

    List<ProductType> mostUsedToppings();
}