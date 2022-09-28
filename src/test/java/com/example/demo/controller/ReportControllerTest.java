package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.enums.ProductType;
import com.example.demo.exception.ExceptionTranslator;
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

    @Autowired
    private MockMvc mockMvc;

/*    private List<ProductType> getMostUsedToppings() {
        ProductDto topping1 = new ProductDto(3, "Milk", ProductType.TOPPINGS, BigDecimal.valueOf(2));
        MostUsedProduct mostUsedProduct1 = MostUsedProduct.builder().product(topping1).count(5L).build();
        ProductDto topping2 = new ProductDto(4, "Hazelnut syrup", ProductType.TOPPINGS, BigDecimal.valueOf(3));
        MostUsedProduct mostUsedProduct2 = MostUsedProduct.builder().product(topping2).count(3L).build();
        ProductDto topping3 = new ProductDto(5, "Chocolate sauce", ProductType.TOPPINGS, BigDecimal.valueOf(5));
        MostUsedProduct mostUsedProduct3 = MostUsedProduct.builder().product(topping3).count(2L).build();
        return List.of(mostUsedProduct1, mostUsedProduct2, mostUsedProduct3);
    }*/

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
    public void findMostUsedToppingsForDrinks_customerId_OrderReportDto() throws Exception {
//        when(reportService.mostUsedToppings()).thenReturn(getMostUsedToppings());
        mockMvc.perform(get("/admin/reports/toppings").header("customerId", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].count").value(5))
                .andExpect(jsonPath("$.[0].product.name").value("Milk"));
    }
}
