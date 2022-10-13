package com.example.demo.utils;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Order;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderItemDto;
import com.example.demo.enums.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class Calculate {

    public static BigDecimal calculateBones(Cart cart) {
        boolean freeTopping = false;
        BigDecimal oneFreeTopping = BigDecimal.ZERO;
        boolean discount = false;
        BigDecimal discount_25Percent = BigDecimal.ZERO;

        BigDecimal totalAmount = cart.getItems().stream().map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        List<CartItem> toppingsList = cart.getItems().stream().filter(orderDetails -> orderDetails.getProduct().getType().equals(ProductType.TOPPINGS)).collect(Collectors.toList());
        Optional<CartItem> minPriceToppingItem = toppingsList.stream().min(Comparator.comparing(cartItemDto -> cartItemDto.getProduct().getPrice()));

        boolean drinksItems = cart.getItems().stream().filter(cartItemDto -> cartItemDto.getProduct().getType().equals(ProductType.COFFEE)).collect(Collectors.toList()).size() >= 3;
        if (drinksItems) {
            freeTopping = true;
            oneFreeTopping = totalAmount.subtract(minPriceToppingItem.get().getProduct().getPrice());
        }

        if (totalAmount.compareTo(BigDecimal.valueOf(12)) >= 0) {
            discount = true;
            discount_25Percent = totalAmount.subtract(totalAmount.multiply(BigDecimal.valueOf(25)).divide(BigDecimal.valueOf(100)));
        }

        if (freeTopping && discount)
            if (discount_25Percent.compareTo(oneFreeTopping) >= 0)
                totalAmount = oneFreeTopping;
            else if (oneFreeTopping.compareTo(discount_25Percent) >= 0)
                totalAmount = discount_25Percent;

        return totalAmount;
    }
}