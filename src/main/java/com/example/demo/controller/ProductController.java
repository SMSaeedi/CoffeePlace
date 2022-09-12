package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    List<ProductDto> getProducts() {
        return productService.getProduct();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductDto newProduct(@RequestBody ProductDto productDto) {
        return productService.newProduct(productDto);
    }

    @PutMapping("/{productId}")
    ProductDto updateProduct(@PathVariable int productId,
                                   @RequestBody ProductDto productDto) {
        return productService.updateProduct(productId, productDto);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeProduct(@PathVariable int productId) {
        productService.removeProduct(productId);
    }
}