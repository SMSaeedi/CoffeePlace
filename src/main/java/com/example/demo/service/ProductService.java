package com.example.demo.service;

import com.example.demo.dao.entity.Product;
import com.example.demo.dto.ProductDto;
import com.example.demo.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDto> getProduct();

    Product getProductById(int productId);
    ProductDto newProduct(ProductDto productDto);
    ProductDto updateProduct(int productId, ProductDto productDto);
    void removeProduct(int productId);

    BigDecimal getTotalAmountPerCustomer(int token);

    List<ProductType> mostUsedToppings();
}