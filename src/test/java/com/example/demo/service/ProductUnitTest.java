package com.example.demo.service;

import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.ProductRepository;
import com.example.demo.enums.ProductType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductUnitTest {
    @Mock
    private ProductRepository repository;

    private ProductType productType;

    private List<Product> getProductByType() {
        List<Product> pType = new ArrayList<>();
        pType.add(new Product(1, "Black Coffee", ProductType.COFFEE, BigDecimal.valueOf(4)));
        pType.add(new Product(2, "Latte", ProductType.COFFEE, BigDecimal.valueOf(5)));
        pType.add(new Product(3, "Mocha", ProductType.COFFEE, BigDecimal.valueOf(6)));
        pType.add(new Product(4, "Tea", ProductType.COFFEE, BigDecimal.valueOf(3)));
        return pType;
    }

    @Test
    void shouldParse(){
        given(this.repository.findAllByType(ProductType.TOPPINGS)).willReturn(getProductByType());
//        assertThat(petType.getName()).isEqualTo("Bird");
    }
}