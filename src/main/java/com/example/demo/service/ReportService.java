package com.example.demo.service;

import com.example.demo.dao.entity.Order;
import com.example.demo.dao.entity.OrderItem;
import com.example.demo.dao.entity.Product;
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
import java.util.*;
import java.util.stream.Collectors;

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
        return toOrderDto(order).getTotalAmount();
    }

    public Map<String, Integer> mostUsedToppings() {
        LogInfo.logger.info("mostUsedToppings ");
        Map<String, Integer> map = new HashMap<>();

        List<OrderItem> toppings = orderItemRepository.findAllByProduct_Type(ProductType.TOPPINGS);
        Set<OrderItem> itemSet = new HashSet<>(toppings);
        itemSet.stream().forEachOrdered(orderItem -> {
            List<OrderItem> collect = toppings.stream()
                    .filter(s -> s.getProduct().getId().equals(orderItem.getProduct().getId())).collect(Collectors.toList());
                    map.put(orderItem.getProduct().getName(), collect.stream()
                    .mapToInt(value -> value.getQuantity()).sum());
        });
        /*for (OrderItem o : itemSet) {
            quantitySum = toppings.stream()
                    .filter(s -> s.getProduct().getId().equals(o.getProduct().getId()))
                    .mapToInt(value -> value.getQuantity()).sum();
            map.put(o.getProduct().getName(), quantitySum);
        }*/
        return map;
    }

    private OrderDto toOrderDto(Order order) {
        return mapper.convertValue(order, OrderDto.class);
    }
}