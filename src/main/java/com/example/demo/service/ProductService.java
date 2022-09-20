package com.example.demo.service;

import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.ProductRepository;
import com.example.demo.dto.ProductDto;
import com.example.demo.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    @Value("${service.product.exception.productNotFound}")
    String productNotFound;

    private final ProductRepository productRepository;
    private final ObjectMapper mapper;

    public ProductService(ProductRepository product, ObjectMapper modelMapper) {
        this.productRepository = product;
        this.mapper = modelMapper;
    }

    public List<ProductDto> getProduct() {
        log.debug("getProduct ");
        return mapper.convertValue(productRepository.findAll(), List.class);
    }


    public Product getProductById(int productId) {
        log.debug("getProductById ", productId);
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("No such product found!"));
    }

    public ProductDto newProduct(ProductDto productDto) {
        log.debug("newProduct ", productDto);
        Product newProduct = Product.builder()
                .name(productDto.getName())
                .type(productDto.getType())
                .price(productDto.getPrice())
                .build();
        return mapper.convertValue(productRepository.save(newProduct), ProductDto.class);
    }

    public ProductDto updateProduct(int productId, ProductDto productDto) {
        log.debug("updateProduct ", productId, productDto);
        Product productById = getProductById(productId);
        Product newProduct = productById.builder()
                .name(productDto.getName())
                .type(productDto.getType())
                .price(productDto.getPrice())
                .build();
        return mapper.convertValue(productRepository.save(newProduct), ProductDto.class);
    }

    public void removeProduct(int productId) {
        log.debug("deleteProduct ", productId);
        Optional<Product> productById = productRepository.findById(productId);

        if (productById.isPresent())
            productRepository.deleteById(productId);
        productById.orElseThrow(() -> new NotFoundException(productNotFound));
    }
}