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
    OrderDto createOrder(@RequestHeader int token,
                         @RequestBody OrderDto newOrder) {
        return orderService.createOrder(token, newOrder);
    }

    @GetMapping
    OrderDto getOrderByCustomerId(@RequestHeader int token){
        return orderService.getOrderByCustomerId(token);
    }
}