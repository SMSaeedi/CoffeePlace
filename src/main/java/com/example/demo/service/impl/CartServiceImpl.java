package com.example.demo.service.impl;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.repository.CartItemRepository;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dto.CartDto;
import com.example.demo.dao.exception.NotFoundException;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Value("${service.cart.impl.exception.cartNotFound}")
    String cartNotFound;

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
        log.debug("getCartByToken ");
        return mapper.convertValue(findCartByCustomerId(token), CartDto.class);
    }

    @Override
    public CartDto addCart(int token, int productId) {
        log.debug("addCart ", productId);
        Cart cart = cartRepository.findByCustomerId(token)
                .orElseGet(() -> cartRepository.save(Cart.builder()
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
        log.debug("updateCart ", productId);
        Cart cart = findCartByCustomerId(token);

        CartItem item = cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(productId)).findFirst()
                .orElse(cartItemRepository.save(CartItem.builder()
                        .product(productService.getProductById(productId))
                        .quantity(quantity)
                        .build()));

        cart.getItems().remove(item);

        if (quantity != 0) {
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }

        Cart updatedCart = cartRepository.save(cart);

        return mapper.convertValue(updatedCart, CartDto.class);
    }

    private Cart findCartByCustomerId(int token) {
        log.debug("findCartByCustomerId ");
        return cartRepository.findByCustomerId(token).orElseThrow(() -> new NotFoundException(cartNotFound));
    }

    @Override
    public void deleteCart(int token, int cartId) {
        log.debug("deleteCart ", cartId);
        Optional<Cart> cartByCustomerId = cartRepository.findByCustomerId(token);

        if (cartByCustomerId.isPresent())
            cartRepository.deleteById(cartId);
        cartByCustomerId.orElseThrow(() -> new NotFoundException(cartNotFound));
    }
}