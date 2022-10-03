package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderItemDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.ExceptionTranslator;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ReportControllerTest {
    @Autowired
    private ReportController controller;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @MockBean
    ProductService productService;

    @MockBean
    ReportService reportService;

    @MockBean
    OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    private List<ProductDto> getProducts() {
        ProductDto product1 = new ProductDto("Milk", ProductType.TOPPINGS, BigDecimal.valueOf(2));
        ProductDto product2 = new ProductDto("Hazelnut syrup", ProductType.TOPPINGS, BigDecimal.valueOf(3));
        ProductDto product3 = new ProductDto("Chocolate sauce", ProductType.TOPPINGS, BigDecimal.valueOf(5));
        return List.of(product1, product2, product3);
    }

    private List<OrderItemDto> orderItems() {
        OrderItemDto item1 = new OrderItemDto(getProducts().get(0), 2, getProducts().get(0).getPrice());
        OrderItemDto item2 = new OrderItemDto(getProducts().get(1), 1, getProducts().get(1).getPrice());
        OrderItemDto item3 = new OrderItemDto(getProducts().get(0), 3, getProducts().get(0).getPrice());
        OrderItemDto item4 = new OrderItemDto(getProducts().get(1), 2, getProducts().get(1).getPrice());
        OrderItemDto item5 = new OrderItemDto(getProducts().get(2), 1, getProducts().get(2).getPrice());
        OrderItemDto item6 = new OrderItemDto(getProducts().get(1), 3, getProducts().get(1).getPrice());
        return List.of(item1, item2, item3, item4, item5, item6);
    }

    private OrderDto getOrder() {
        return new OrderDto(orderItems(), BigDecimal.valueOf(25), "hot and decaf please");
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(exceptionTranslator)
                .build();
    }

    @Test
    public void findCustomerOrdersReport_customerId_OrderReportDto() throws Exception {
        when(reportService.getTotalAmountPerCustomer(1)).thenReturn(BigDecimal.valueOf(5.00));
        mockMvc.perform(get("/admin/reports/customers/1/orders/status").header("customerId", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalAmountOfOrders").value(5.00));
    }

    @Test
    public void findMostUsedToppings() throws Exception {
//        when(orderService.createOrder(1, getOrder())).thenReturn(getOrder());
//        when(reportService.mostUsedToppings()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/reports/topToppings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}
