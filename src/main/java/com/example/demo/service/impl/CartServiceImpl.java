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

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

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
        return mapper.convertValue(findCartByCustomerId(token), CartDto.class);
    }

    @Override
    public CartDto newCart(int token, int productId) {
        Cart cart = cartRepository.findByCustomerId(token)
                .orElseGet(() ->  cartRepository.save(Cart.builder()
                        .customerId(token)
                        .items(new ArrayList<>())
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
        Cart cart = findCartByCustomerId(token);

        CartItem item = cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(productId)).findFirst()
                .orElse(cartItemRepository.save(CartItem.builder()
                        .product(productService.getProductById(productId))
                        .quantity(quantity)
                        .build()));


        if (quantity == 0)
            cart.getItems().remove(item);
        else if (quantity != 0)
            cart.getItems().add(item);

        Cart updatedCart = cartRepository.save(cart);

        return mapper.convertValue(updatedCart, CartDto.class);
    }

    private Cart findCartByCustomerId(int token) {
        return cartRepository.findByCustomerId(token).orElseThrow(() -> new NotFoundException("No such cart found!"));
    }

    @Override
    public void deleteCart(int token, int cartId) {
        Optional<Cart> cartByCustomerId = cartRepository.findByCustomerId(token);

        if (cartByCustomerId.isPresent())
            cartRepository.deleteById(cartId);
        cartByCustomerId.orElseThrow(() -> new NotFoundException("No cart found for deleting!"));
    }
}