package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OrderDto createOrder(@RequestHeader int customerId,
                         @RequestBody OrderDto newOrder) {
        return orderService.createOrder(customerId, newOrder);
    }

    @GetMapping
    OrderDto getOrderByCustomerId(@RequestHeader int customerId) {
        return orderService.getOrderByCustomerId(customerId);
    }

    @GetMapping("/{orderItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void cancelOrderItem(@PathVariable int orderItemId) {
        orderService.cancelOrderItem(orderItemId);
    }
}