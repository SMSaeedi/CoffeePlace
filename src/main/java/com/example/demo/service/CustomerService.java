package com.example.demo.service;

import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.TokenDto;

public interface CustomerService {
    TokenDto registerCustomer(CustomerDto request);
}