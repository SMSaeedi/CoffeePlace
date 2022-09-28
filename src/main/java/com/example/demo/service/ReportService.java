package com.example.demo.service;

import com.example.demo.dao.entity.Order;
import com.example.demo.dao.repository.OrderItemRepository;
import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.dto.OrderDto;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.log.LogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportService {
    @Value("${service.order.exception.orderNotFound}")
    private String orderNotFound;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ObjectMapper mapper;

    public ReportService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.mapper = mapper;
    }

    public BigDecimal getTotalAmountPerCustomer(int customerId) {
        LogInfo.logger.info("getTotalAmountPerCustomer ");
        Order order = orderRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(orderNotFound));
        OrderDto orderDto = mapper.convertValue(order, OrderDto.class);
        return orderDto.getTotalAmount();
    }

    public List<ProductType> mostUsedToppings() {
        LogInfo.logger.info("mostUsedToppings ");
        orderItemRepository.findAllByProduct();
        return null;
    }
}