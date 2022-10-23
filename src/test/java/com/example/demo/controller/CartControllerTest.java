package com.example.demo.controller;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dao.repository.CustomerRepository;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.ExceptionTranslator;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private CartController cartController;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @MockBean
    CartService cartService;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(exceptionTranslator)
                .build();
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

    private Cart cartsModel() {
        Set<CartItem> cartItems = objectMapper.convertValue(getCartItemList(), Set.class);
        return Cart.builder()
                .id(sequenceId())
                .items(cartItems)
                .customerId(1)
                .build();
    }

    public List<CartItemDto> getCartItemList() {
        ProductDto drink1 = new ProductDto("Black Coffee", ProductType.COFFEE, BigDecimal.valueOf(4));
        CartItemDto item1 = CartItemDto.builder().product(drink1).quantity(1).build();
        ProductDto drink2 = new ProductDto("Latte", ProductType.COFFEE, BigDecimal.valueOf(5));
        CartItemDto item2 = CartItemDto.builder().product(drink2).quantity(1).build();
        ProductDto drink3 = new ProductDto("Mocha", ProductType.COFFEE, BigDecimal.valueOf(6));
        CartItemDto item3 = CartItemDto.builder().product(drink3).quantity(1).build();
        ProductDto topping1 = new ProductDto("Milk", ProductType.TOPPINGS, BigDecimal.valueOf(2));
        CartItemDto item4 = CartItemDto.builder().product(topping1).quantity(1).build();

        return List.of(item1, item2, item3, item4);
    }

    @Test
    public void getCartByCustomerId() throws Exception {
        when(cartService.getCartByCustomerId(1)).thenReturn(CartDto.builder().items(getCartItemList()).build());
        mockMvc.perform(get("/api/carts/").header("customerId", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(4))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void addCartForACustomer() throws Exception {
        when(customerRepository.findById(1)).thenReturn(Optional.of(Customer.builder().id(1).build()));
        when(cartRepository.findByCustomerId(1)).thenReturn(Optional.of(cartsModel()));
        when(cartService.addCartAndItem(1, 1)).thenReturn(CartDto.builder().items(getCartItemList()).build());
        mockMvc.perform(post("/api/carts/1").header("customerId", 1))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quantity").value(1));
    }

    @Test
    public void createCart_andPassNoCustomerId_throwsNotAcceptableException() throws Exception {
        mockMvc.perform(post("/api/carts/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("test BadRequestException"));
    }

    @Test
    public void deleteCartForACustomer_increaseQuantity() throws Exception {
        mockMvc.perform(delete("/api/carts/3"))
                .andExpect(status().isNoContent());
    }
}