package com.example.demo.service;

import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.repository.CustomerRepository;
import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CustomerRepository customerRepository;

    private List<Customer> customers() {
        List<Customer> list = new ArrayList<>();
        Customer empOne = new Customer(sequenceId(), "Mahsa Saeedi", "mahsasaeedy@gmail.com", "Nr.50, Azadi St. Azadi Avenue");
        Customer empTwo = new Customer(sequenceId(), "Alex Veldaviny", "alexk@yahoo.com", "Nr.51, Azadi St. Azadi Avenue");
        Customer empThree = new Customer(sequenceId(), "Steve Martiny", "swaugh@gmail.com", "Nr.52, Azadi St. Azadi Avenue");

        list.add(empOne);
        list.add(empTwo);
        list.add(empThree);

        return list;
    }

    private int sequenceId() {
        List<Integer> ret = IntStream.rangeClosed(10, 50).boxed().collect(Collectors.toList());

        int total = 0;
        for (int i : ret) {
            total += i;
            return total;
        }
        return total;
    }

    @Test
    public void createNewCustomer() {
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customers().get(0));

        TokenDto tokenDto = customerService.registerCustomer(objectMapper.convertValue(customers().get(0), CustomerDto.class));

        assertEquals(tokenDto.getToken(), customers().get(0).getId());
    }

    @Test
    public void findAllCustomers() {
        when(customerRepository.findAll()).thenReturn(customers());

        assertEquals(3, customers().size());
        verify(customerRepository, times(0)).findById(1);
    }

    @Test
    public void findCustomerBy_existedId() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customers().get(1)));

        Optional<Customer> customer = customerRepository.findById(1);

        assertEquals(customers().get(1).getName(), customer.get().getName());
        assertEquals(customers().get(1).getEmail(), customer.get().getEmail());
    }

    @Test
    public void findCustomerBy_notExistedId() {
        Throwable exception = assertThrows(RuntimeException.class, () -> customerRepository.findById(1011111).orElseThrow());
        assertEquals("No value present", exception.getMessage());
    }
}