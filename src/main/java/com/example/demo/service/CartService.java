package com.example.demo.service;

import com.example.demo.dto.CartDto;

public interface CartService {
    CartDto getCartByToken(int token);
    CartDto newCart(int token, int productId);

    CartDto updateCart(int token, int productId, int quantity);

    void deleteCart(int token, int cartId);
}