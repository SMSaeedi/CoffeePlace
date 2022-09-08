package com.example.demo.service.impl;

import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.repository.CustomerRepository;
import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public TokenDto registerCustomer(CustomerDto dto) throws TransactionSystemException {
        Customer customer = customerRepository.save(Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build());
        return TokenDto.builder().token(customer.getId()).build();
    }
}