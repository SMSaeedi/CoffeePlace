package com.example.demo.controller;

import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TokenDto newCustomer(@Validated @RequestBody CustomerDto request) {
        return customerService.registerCustomer(request);
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    CustomerDto updateCustomer(@PathVariable int customerId, @Validated @RequestBody CustomerDto request) {
        return customerService.updateCustomer(customerId, request);
    }
}