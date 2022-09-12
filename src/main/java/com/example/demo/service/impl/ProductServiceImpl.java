package com.example.demo.service.impl;

import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.ProductRepository;
import com.example.demo.dto.ProductDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper modelMapper;

    public ProductServiceImpl(ProductRepository product, ObjectMapper modelMapper) {
        this.productRepository = product;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDto> getProduct() {
        return modelMapper.convertValue(productRepository.findAll(), List.class);
    }


    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("No such product found!"));
    }

    public void removeProduct(int productId) {
        productRepository.deleteById(productId);
    }
}