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
    CartDto newCart(@RequestHeader int token,
                    @PathVariable int productId) {
        return cartService.newCart(token, productId);
    }

    @PutMapping("/{productId}/{quantity}")
    CartDto updateCart(@RequestHeader int token,
                       @PathVariable int productId,
                       @PathVariable int quantity) {
        return cartService.updateCart(token, productId, quantity);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeCart(@RequestHeader int token,
                    @PathVariable Integer cartId) {
        cartService.deleteCart(token,cartId);
    }
}