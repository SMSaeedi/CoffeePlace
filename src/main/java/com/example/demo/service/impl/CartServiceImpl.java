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

        Cart addNewItem = cartRepository.save(cart);

        return mapper.convertValue(addNewItem, CartDto.class);
    }

    @Override
    public CartDto updateCartItem(int token, int cartItemId, int productId, int quantity) {
        log.debug("updateCart ", productId);
        Cart cart = findCartByCustomerId(token);


        CartItem item = cart.getItems().stream().filter(cartItem -> cartItem.getId().equals(cartItemId)
                        && cartItem.getProduct().getId().equals(productId)).findFirst()
                .orElseGet(() -> cartItemRepository.save(CartItem.builder()
                        .id(cartItemId)
                        .product(productService.getProductById(productId))
                        .quantity(quantity)
                        .build()));

        cart.getItems().remove(item);

        if (quantity != 0)
            item.setQuantity(quantity);

        CartItem updatedCartItem = cartItemRepository.save(item);
        cart.getItems().add(updatedCartItem);

        return mapper.convertValue(cart, CartDto.class);
    }

    private Cart findCartByCustomerId(int token) {
        log.debug("findCartByCustomerId ");
        return cartRepository.findByCustomerId(token).orElseThrow(() -> new NotFoundException(cartNotFound));
    }

    @Override
    public void deleteCart(int cartItemId) {
        log.debug("deleteCart ", cartItemId);
        Optional<CartItem> cartByCustomerId = cartItemRepository.findById(cartItemId);

        if (cartByCustomerId.isPresent())
            cartItemRepository.deleteById(cartItemId);
        cartByCustomerId.orElseThrow(() -> new NotFoundException(cartNotFound));
    }
}