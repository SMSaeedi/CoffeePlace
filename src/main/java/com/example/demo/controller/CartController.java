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
    CartDto getCartByCustomerId(@RequestHeader int customerId) {
        return cartService.getCartByCustomerId(customerId);
    }

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    CartDto addCartAndItem(@RequestHeader int customerId,
                    @PathVariable int productId) {
        return cartService.addCartAndItem(customerId, productId);
    }

    @PutMapping("/{cartItemId}/{productId}/{quantity}")
    CartDto updateCartItem(@RequestHeader int customerId,
                       @PathVariable int cartItemId,
                       @PathVariable int productId,
                       @PathVariable int quantity) {
        return cartService.updateCartItem(customerId, cartItemId, productId, quantity);
    }

    @DeleteMapping("/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeCartItem(@PathVariable int cartItemId) {
        cartService.removeCartItem(cartItemId);
    }
}