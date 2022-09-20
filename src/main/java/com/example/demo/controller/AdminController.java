package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.enums.ProductType;
import com.example.demo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    List<ProductDto> getProducts() {
        return productService.getProduct();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductDto newProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.newProduct(productDto);
    }

    @PutMapping("/{productId}")
    ProductDto updateProduct(@PathVariable int productId,
                             @Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(productId, productDto);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeProduct(@PathVariable int productId) {
        productService.removeProduct(productId);
    }

    @GetMapping("/totalAmount")
    BigDecimal getTotalAmountPerCustomer(@RequestHeader int token) {
        return productService.getTotalAmountPerCustomer(token);
    }

    @GetMapping("/topToppings")
    List<ProductType> mostUsedToppings() {
        return productService.mostUsedToppings();
    }
}