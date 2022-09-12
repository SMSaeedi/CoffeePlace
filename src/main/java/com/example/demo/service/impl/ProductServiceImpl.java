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
    private final ObjectMapper mapper;

    public ProductServiceImpl(ProductRepository product, ObjectMapper modelMapper) {
        this.productRepository = product;
        this.mapper = modelMapper;
    }

    @Override
    public List<ProductDto> getProduct() {
        return mapper.convertValue(productRepository.findAll(), List.class);
    }


    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("No such product found!"));
    }

    @Override
    public ProductDto newProduct(ProductDto productDto) {
        Product newProduct = Product.builder()
                .name(productDto.getName())
                .type(productDto.getType())
                .price(productDto.getPrice())
                .build();
        return mapper.convertValue(productRepository.save(newProduct), ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(int productId, ProductDto productDto) {
        Product productById = getProductById(productId);
        Product newProduct = productById.builder()
                .name(productDto.getName())
                .type(productDto.getType())
                .price(productDto.getPrice())
                .build();
        return mapper.convertValue(productRepository.save(newProduct), ProductDto.class);
    }

    public void removeProduct(int productId) {
        productRepository.deleteById(productId);
    }
}