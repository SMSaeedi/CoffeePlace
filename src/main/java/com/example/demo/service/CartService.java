package com.example.demo.service;

import com.example.demo.dto.CartDto;

public interface CartService {
    CartDto getCartByToken(int token);
    CartDto addCart(int token, int productId);

    CartDto updateCartItem(int token,int cartItemId, int productId, int quantity);

    void deleteCart(int cartItemId);
}