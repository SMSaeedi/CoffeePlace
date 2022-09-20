package com.example.demo.service;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.repository.CartItemRepository;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dto.CartDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.log.LogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class CartService {

    @Value("${service.cart.exception.cartNotFound}")
    String cartNotFound;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final ObjectMapper mapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService, ObjectMapper mapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.mapper = mapper;
    }

    public CartDto getCartByCustomerId(int customerId) {
        LogInfo.logger.info("getCartByToken ");
        return mapper.convertValue(findCartByCustomerId(customerId), CartDto.class);
    }

    public CartDto addCartAndItem(int token, int productId) {
        LogInfo.logger.info("addCart ", productId);
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

    public CartDto updateCartItem(int customerId, int cartItemId, int productId, int quantity) {
        LogInfo.logger.info("updateCart ", productId);
        Cart cart = findCartByCustomerId(customerId);


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

    private Cart findCartByCustomerId(int customerId) {
        LogInfo.logger.info("findCartByCustomerId ");
        return cartRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(cartNotFound));
    }

    public void removeCartItem(int cartItemId) {
        LogInfo.logger.info("removeCartItem ", cartItemId);
        Optional<CartItem> cartItemById = cartItemRepository.findById(cartItemId);

        if (cartItemById.isPresent())
            cartItemRepository.deleteById(cartItemId);
        cartItemById.orElseThrow(() -> new NotFoundException(cartNotFound));
    }
}