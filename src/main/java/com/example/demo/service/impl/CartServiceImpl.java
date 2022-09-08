package com.example.demo.service.impl;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.repository.CartItemRepository;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dto.CartDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    public static boolean toppingBonus;
    public static boolean discount;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final ObjectMapper mapper;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService, ObjectMapper mapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.mapper = mapper;
    }

    @Override
    public CartDto getCartByToken(int token) {
        return mapper.convertValue(cartRepository.findByCustomerId(token).orElseThrow(() -> new NotFoundException("No such cart found!")), CartDto.class);
    }

    @Override
    public CartDto addCart(int token, int productId) {
        Cart cart = cartRepository.findByCustomerId(token)
                .orElse(cartRepository.save(Cart.builder()
                        .customerId(token)
                        .items(new HashSet<>())
                        .build()));

        CartItem newItem = cartItemRepository.save(CartItem.builder()
                .product(productService.getProductById(productId))
                .quantity(1)
                .build());

        cart.getItems().add(newItem);

        Cart updatedCart = cartRepository.save(cart);

        return mapper.convertValue(updatedCart, CartDto.class);
    }

    @Override
    public CartDto updateCart(int token, int productId, int quantity) {
        Optional<Cart> cart = cartRepository.findByCustomerId(token);

        cart.get().getItems().forEach(cartItem -> {
            if (cartItem.getProduct().getId().equals(productId))
                cartItem.setQuantity(quantity);
        });

        Cart updatedCart = cartRepository.save(cart.get());

        return mapper.convertValue(updatedCart, CartDto.class);
    }

    @Override
    public void deleteCart(int cartId) {
        cartRepository.deleteById(cartId);
    }
}