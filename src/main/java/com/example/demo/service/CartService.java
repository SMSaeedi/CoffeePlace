package com.example.demo.service;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Customer;
import com.example.demo.dao.entity.Product;
import com.example.demo.dao.repository.CartItemRepository;
import com.example.demo.dao.repository.CartRepository;
import com.example.demo.dto.CartDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.log.LogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CartService {

    @Value("${service.cart.exception.cartNotFound}")
    String cartNotFound;

    @Value("${service.cartItem.exception.cartItemNotFound}")
    String cartItemNotFound;

    @Value("${service.customer.exception.customerNotFound}")
    String customerNotFound;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final ObjectMapper mapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, CustomerService customerService, ProductService productService, ObjectMapper mapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.mapper = mapper;
    }

    public CartDto getCartByCustomerId(int customerId) {
        LogInfo.logger.info("getCartByToken ");
        return toCartDto(findCartByCustomerId(customerId));
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CartDto addCartAndItem(int customerId, int productId) {
        LogInfo.logger.info("addCart ", productId);
        customerService.findCustomerById(customerId).orElseThrow(() -> new NotFoundException(customerNotFound));
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .customerId(customerId)
                        .items(new HashSet<>())
                        .build()));

        CartItem newItem = cartItemRepository.save(CartItem.builder().
                cartId(cart.getId()).product(productService.getProductById(productId)).quantity(1).build());

        cart.getItems().add(newItem);

        Cart addNewItem = cartRepository.save(cart);

        return toCartDto(addNewItem);
    }

    public CartDto updateCartItem(int customerId, int cartItemId, int productId, int quantity) {
        LogInfo.logger.info("updateCart ", cartItemId);
        Cart cart = findCartByCustomerId(customerId);
        CartItem cartItem = findCartItemById(cartItemId);
        Product productById = productService.getProductById(productId);

        cartItem = cartItemRepository.save(CartItem.builder()
                .id(cartItemId)
                .cartId(cart.getId())
                .product(productById)
                .quantity(quantity)
                .build());

        cart.getItems().remove(cartItem);

        if (quantity != 0) cartItem.setQuantity(quantity);

        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        cart.getItems().add(updatedCartItem);

        return toCartDto(cart);
    }

    private Cart findCartByCustomerId(int customerId) {
        LogInfo.logger.info("findCartByCustomerId ");
        return cartRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException(cartNotFound));
    }

    private CartItem findCartItemById(int cartItemId) {
        LogInfo.logger.info("findCartItemById ");
        return cartItemRepository.findById(cartItemId).orElseThrow(() -> new NotFoundException(cartItemNotFound));
    }

    public void removeCartItem(int cartItemId) {
        LogInfo.logger.info("removeCartItem ", cartItemId);
        Optional<CartItem> cartItemById = cartItemRepository.findById(cartItemId);

        if (cartItemById.isPresent()) cartItemRepository.deleteById(cartItemId);
        throw new NotFoundException(cartNotFound);
    }

    private CartDto toCartDto(Cart cart) {
        return mapper.convertValue(cart, CartDto.class);
    }
}