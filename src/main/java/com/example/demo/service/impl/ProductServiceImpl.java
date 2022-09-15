package com.example.demo.service.impl;

import com.example.demo.dao.entity.CustomerOrder;
import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.OrderRepository;
import com.example.demo.dao.repository.ProductRepository;
import com.example.demo.dto.ProductDto;
import com.example.demo.enums.ProductType;
import com.example.demo.dao.exception.NotFoundException;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${service.order.impl.exception.orderNotFound}")
    private String orderNotFound;

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper mapper;

    public ProductServiceImpl(ProductRepository product, OrderRepository orderRepository, ObjectMapper modelMapper) {
        this.productRepository = product;
        this.orderRepository = orderRepository;
        this.mapper = modelMapper;
    }

    @Override
    public List<ProductDto> getProduct() {
        log.debug("getProduct ");
        return mapper.convertValue(productRepository.findAll(), List.class);
    }


    public Product getProductById(int productId) {
        log.debug("getProductById ", productId);
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("No such product found!"));
    }

    @Override
    public ProductDto newProduct(ProductDto productDto) {
        log.debug("newProduct ", productDto);
        Product newProduct = Product.builder()
                .name(productDto.getName())
                .type(productDto.getType())
                .price(productDto.getPrice())
                .build();
        return mapper.convertValue(productRepository.save(newProduct), ProductDto.class);
    }

    @Override
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
        log.debug("removeProduct ", productId);
        productRepository.deleteById(productId);
    }

    @Override
    public BigDecimal getTotalAmountPerCustomer(int token) {
        log.debug("getTotalAmountPerCustomer ");
        CustomerOrder customerOrder = orderRepository.findByCustomerId(token).orElseThrow(() -> new NotFoundException(orderNotFound));
        return customerOrder.getOrderItems().stream().map(orderItemDto -> (orderItemDto.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItemDto.getQuantity())))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public List<ProductType> mostUsedToppings() {
        log.debug("mostUsedToppings ");
        List<CustomerOrder> allOrders = orderRepository.findAll();
        allOrders.stream().map(customerOrder -> new ArrayList<>(customerOrder.getOrderItems()).stream()
                .map(orderItem -> orderItem.getProduct().getType().equals(ProductType.TOPPINGS)).collect(Collectors.toList()));
        return null;
    }
}