package com.example.demo.service;

import com.example.demo.dao.entity.Order;
import com.example.demo.dao.repository.OrderItemRepository;
import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.dto.MostUsedProduct;
import com.example.demo.dto.OrderDto;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
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
        log.debug("getTotalAmountPerCustomer ");
        Order order = orderRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(orderNotFound));
        OrderDto orderDto = mapper.convertValue(order, OrderDto.class);
        return orderDto.getTotalAmount();
    }

    public List<MostUsedProduct> mostUsedToppings(ProductType productType) {
        log.debug("mostUsedToppings ");
        return orderItemRepository.mostUsedProducts(productType);
    }
}