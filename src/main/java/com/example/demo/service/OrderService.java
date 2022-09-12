package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    BigDecimal getTotalAmountPerCustomer(int token);

    List<ProductType> mostUsedToppings();

    OrderDto createOrder(int token, OrderDto orderDto);

    OrderDto updateOrder(int token, int orderId, OrderDto orderDto);

    void deleteOrder(int token, int orderId);
}