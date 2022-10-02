package com.example.demo.service;

import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.repository.CustomerRepository;
import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.log.LogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        LogInfo.logger.info("registerCustomer ", dto);
        Customer customer = customerRepository.save(Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .postalAddress(dto.getPostalAddress())
                .build());
        return TokenDto.builder().token(customer.getId()).build();
    }

    public CustomerDto updateCustomer(int customerId, CustomerDto dto) {
        LogInfo.logger.info("updateCustomer ", dto);
        Customer findCustomerById = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException(customerNotFound));
        Customer customer = customerRepository.save(findCustomerById.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .postalAddress(dto.getPostalAddress())
                .build());
        return toCustomerDto(customer);
    }

    private CustomerDto toCustomerDto(Customer customer) {
        return mapper.convertValue(customer, CustomerDto.class);
    }
}