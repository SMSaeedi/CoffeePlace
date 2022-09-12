package com.example.demo.service.impl;

import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.dto.OrderDto;
import com.example.demo.enums.ProductType;
import com.example.demo.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public BigDecimal getTotalAmountPerCustomer(int token) {
        return null;
    }

    @Override
    public List<ProductType> mostUsedToppings() {
        return null;
    }

    @Override
    public OrderDto createOrder(int token, OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto updateOrder(int token, int orderId, OrderDto orderDto) {
        return null;
    }

    @Override
    public void deleteOrder(int token, int orderId) {

    }
}