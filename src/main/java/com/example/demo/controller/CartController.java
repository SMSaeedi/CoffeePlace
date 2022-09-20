package com.example.demo.controller;

import com.example.demo.dto.CartDto;
import com.example.demo.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    CartDto getCart(@RequestHeader int token) {
        return cartService.getCartByToken(token);
    }

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    CartDto addCart(@RequestHeader int token,
                    @PathVariable int productId) {
        return cartService.addCart(token, productId);
    }

    @PutMapping("/{cartItemId}/{productId}/{quantity}")
    CartDto updateCart(@RequestHeader int token,
                       @PathVariable int cartItemId,
                       @PathVariable int productId,
                       @PathVariable int quantity) {
        return cartService.updateCartItem(token, cartItemId, productId, quantity);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeCart(@RequestHeader int token,
                    @PathVariable Integer cartId) {
        cartService.deleteCart(token,cartId);
    }
}