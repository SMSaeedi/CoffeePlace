package com.example.demo.service;

import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.repository.CustomerRepository;
import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

    @Value("${service.cart.exception.cartNotFound}")
    String customerNotFound;

    private final CustomerRepository customerRepository;
    private final ObjectMapper mapper;

    public CustomerService(CustomerRepository customerRepository, ObjectMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public TokenDto registerCustomer(CustomerDto dto) {
        log.debug("registerCustomer ", dto);
        Customer customer = customerRepository.save(Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .postalAddress(dto.getPostalAddress())
                .build());
        return TokenDto.builder().token(customer.getId()).build();
    }

    public CustomerDto updateCustomer(int customerId, CustomerDto dto) {
        log.debug("updateCustomer ", dto);
        Customer findCustomerById = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException(customerNotFound));
        Customer customer = customerRepository.save(findCustomerById.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .postalAddress(dto.getPostalAddress())
                .build());
        return mapper.convertValue(customer, CustomerDto.class);
    }
}