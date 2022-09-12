package com.example.demo.intergrationTest;

import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.ProductRepository;
import com.example.demo.enums.ProductType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository productRepository;

    private Product getProduct() {
        return Product.builder()
                .id(1)
                .type(ProductType.COFFEE)
                .name("Black Coffee")
                .price(BigDecimal.valueOf(4))
                .build();
    }

    private List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        products.add(new Product(1, "Black Coffee", ProductType.COFFEE, BigDecimal.valueOf(4)));
        products.add(new Product(2, "Latte", ProductType.COFFEE, BigDecimal.valueOf(5)));
        products.add(new Product(3, "Mocha", ProductType.COFFEE, BigDecimal.valueOf(6)));
        products.add(new Product(4, "Tea", ProductType.COFFEE, BigDecimal.valueOf(3)));

        products.add(new Product(5, "Milk", ProductType.TOPPINGS, BigDecimal.valueOf(2)));
        products.add(new Product(6, "Hazelnut syrup", ProductType.TOPPINGS, BigDecimal.valueOf(3)));
        products.add(new Product(7, "Chocolate sauce", ProductType.TOPPINGS, BigDecimal.valueOf(5)));
        products.add(new Product(8, "Lemon", ProductType.TOPPINGS, BigDecimal.valueOf(2)));

        return products;
    }

    @Test
    public void createANewProduct() throws Exception {
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(getProduct());

        mockMvc.perform(get("/api/customers")).andExpect(status().isOk()).andExpect(model().attributeExists("productId"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void updateAProduct() throws Exception {
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(getProduct());

        mockMvc.perform(get("/api/customers")).andExpect(status().isOk()).andExpect(model().attributeExists("productId"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}