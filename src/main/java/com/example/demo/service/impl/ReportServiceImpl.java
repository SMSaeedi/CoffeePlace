package com.example.demo.service.impl;

import com.example.demo.dao.entity.CustomerOrder;
import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Value("${service.order.impl.exception.orderNotFound}")
    private String orderNotFound;

    private final OrderRepository orderRepository;

    public ReportServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public BigDecimal getTotalAmountPerCustomer(int token) {
        log.debug("getTotalAmountPerCustomer ");
        CustomerOrder customerOrder = orderRepository.findByCustomerId(token).orElseThrow(() -> new NotFoundException(orderNotFound));
        return customerOrder.getOrderItems().stream().map(orderItemDto -> (orderItemDto.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItemDto.getQuantity())))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public List<ProductType> mostUsedToppings() {
        log.debug("mostUsedToppings ");
        List<CustomerOrder> allOrders = orderRepository.findAll();
        allOrders.stream().map(customerOrder -> new ArrayList<>(customerOrder.getOrderItems()).stream()
                .map(orderItem -> orderItem.getProduct().getType().equals(ProductType.TOPPINGS)).collect(Collectors.toList()));
        return null;
    }

}
