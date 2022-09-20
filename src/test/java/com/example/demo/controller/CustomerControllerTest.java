package com.example.demo.controller;

import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerRepository customerRepository;

    private List<Customer> customers() {
        List<Customer> list = new ArrayList<>();
        Customer empOne = new Customer(sequenceId(), "Mahsa Saeedi", "mahsasaeedy@gmail.com","Nr.49, Azadi St. Azadi Avenue");
        Customer empTwo = new Customer(sequenceId(), "Alex Veldaviny", "alexk@yahoo.com","Nr.50, Azadi St. Azadi Avenue");
        Customer empThree = new Customer(sequenceId(), "Steve Martiny", "swaugh@gmail.com","Nr.51, Azadi St. Azadi Avenue");

        list.add(empOne);
        list.add(empTwo);
        list.add(empThree);

        return list;
    }

    private Customer customer() {
        for (Customer c : customers())
            return c;

        return new Customer(sequenceId(), "Nina Mirzaee", "nina_m@email.com","Nr.48, Azadi St. Azadi Avenue");
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
	public void whenCreateCartWithUserIdAndProductId_thenCorrectResponse() throws Exception {
		when(customerRepository.save(customer())).thenReturn(customer());

		mockMvc.perform(get("/api/customers")).andExpect(status().isOk()).andExpect(model().attributeExists("productId"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

    @Test
    public void findAllCustomers() throws Exception {
        when(customerRepository.save(customer())).thenReturn(customer());

        mockMvc.perform(get("/api/customers")).andExpect(status().isOk()).andExpect(model().attributeExists("productId"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void findCustomerBy_existedId() throws Exception {
        when(customerRepository.save(customer())).thenReturn(customer());

        mockMvc.perform(get("/api/customers")).andExpect(status().isOk()).andExpect(model().attributeExists("productId"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}