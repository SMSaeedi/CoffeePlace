package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.enums.ProductType;
import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    OrderDto createOrder(@RequestHeader int token,
                         @RequestBody OrderDto orderDto) {
        return orderService.createOrder(token, orderDto);
    }

    @PutMapping("/{orderId}")
    OrderDto updateOrder(@RequestHeader int token,
                         @PathVariable int orderId,
                         @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(token, orderId, orderDto);
    }

    @DeleteMapping("/{orderId}")
    void deleteOrder(@RequestHeader int token,
                     @PathVariable int orderId) {
        orderService.deleteOrder(token, orderId);
    }

    @GetMapping
    BigDecimal getTotalAmountPerCustomer(@RequestHeader int token) {
        return orderService.getTotalAmountPerCustomer(token);
    }
//
//    @GetMapping
//    List<ProductType> mostUsedToppings() {
//        return orderService.mostUsedToppings();
//    }

}