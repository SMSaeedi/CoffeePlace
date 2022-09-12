package com.example.demo.service;

import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.repository.CustomerRepository;
import com.example.demo.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class CustomerUnitTest {
    @Mock
    CustomerRepository customerRepository;

    private List<Customer> customers() {
        List<Customer> list = new ArrayList<>();
        Customer empOne = new Customer(sequenceId(), "Mahsa Saeedi", "mahsasaeedy@gmail.com");
        Customer empTwo = new Customer(sequenceId(), "Alex Veldaviny", "alexk@yahoo.com");
        Customer empThree = new Customer(sequenceId(), "Steve Martiny", "swaugh@gmail.com");

        list.add(empOne);
        list.add(empTwo);
        list.add(empThree);

        return list;
    }

    private Customer customer() {
        for (Customer c : customers())
            return c;

        return new Customer(sequenceId(), "Nina Mirzaee", "nina_m@email.com");
    }

    private int sequenceId() {
        List<Integer> ret = IntStream.rangeClosed(1, 50).boxed().collect(Collectors.toList());

        int total = 0;
        for (int i : ret) {
            total += i;
            return total;
        }
        return total;
    }

    @Test
    public void createNewCustomer() {
        customerRepository.save(customer());

        verify(customerRepository, times(1)).save(customer());
    }

    @Test
    public void findAllCustomers() {
        when(customerRepository.findAll()).thenReturn(customers());

        assertEquals(3, customers().size());
        verify(customerRepository, times(0)).findById(1);
    }

    @Test
    public void findCustomerBy_existedId() {
        when(customerRepository.findById(1)).thenReturn(Optional.ofNullable(customer()));

        Optional<Customer> customer = customerRepository.findById(1);

        assertEquals("Mahsa Saeedi", customer.get().getName());
        assertEquals("mahsasaeedy@gmail.com", customer.get().getEmail());
    }

    @Test
    public void findCustomerBy_notExistedId() {
        when(customerRepository.findById(10)).thenReturn(Optional.ofNullable(customer()));

        Throwable exception = assertThrows(NotFoundException.class, () -> customerRepository.findById(10));
        assertEquals(404, ((ResponseStatusException) exception).getStatus().value());
        assertEquals("NOT_FOUND", ((ResponseStatusException) exception).getStatus().name());
    }
}