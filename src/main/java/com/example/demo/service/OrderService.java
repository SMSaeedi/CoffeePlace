package com.example.demo.service;

import com.example.demo.dto.OrderDto;

public interface OrderService {
    OrderDto createOrder(int token, OrderDto newOrder);

    OrderDto getOrderByCustomerId(int token);
}