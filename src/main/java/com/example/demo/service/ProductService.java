package com.example.demo.service;

import com.example.demo.dao.entity.Product;
import com.example.demo.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getProduct();

    Product getProductById(int productId);
}