package com.example.demo.service;

import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.ProductRepository;
import com.example.demo.dto.ProductDto;
import com.example.demo.enums.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    private int sequenceId() {
        List<Integer> ret = IntStream.rangeClosed(10, 50).boxed().collect(Collectors.toList());

        int total = 0;
        for (int i : ret) {
            total += i;
            return total;
        }
        return total;
    }

    private Product newProduct() {
        return Product.builder()
                .id(sequenceId())
                .type(ProductType.COFFEE)
                .name("Green Tea")
                .price(BigDecimal.valueOf(3.5))
                .build();
    }

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
    public void createANewProduct() {
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(newProduct());

        ProductDto productDto = productService.newProduct(objectMapper.convertValue(newProduct(), ProductDto.class));

        assertEquals("Green Tea", productDto.getName());
    }

    @Test
    public void updateAProduct() {
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(getProduct());

        ProductDto productDto = productService.updateProduct(getProduct().getId(), objectMapper.convertValue(getProduct(), ProductDto.class));

        assertEquals(getProducts().get(0).getName(), productDto.getName());
    }

}