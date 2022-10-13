package com.example.demo.utils;

import com.example.demo.dao.entity.Cart;
import com.example.demo.dao.entity.CartItem;
import com.example.demo.dto.OrderDiscount;
import com.example.demo.enums.ProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class OrderDiscountCalculation {
    public static OrderDiscount orderDiscountCal(Cart cart) {
        boolean freeTopping = false;
        BigDecimal oneFreeTopping = BigDecimal.ZERO;
        boolean discount = false;
        BigDecimal discount_25Percent = BigDecimal.ZERO;

        OrderDiscount orderDiscount = OrderDiscount.builder()
                .totalAmount(cart.getItems().stream().map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
                .build();
        List<CartItem> toppingsList = cart.getItems().stream().filter(orderDetails -> orderDetails.getProduct().getType().equals(ProductType.TOPPINGS)).collect(Collectors.toList());
        Optional<CartItem> minPriceToppingItem = toppingsList.stream().min(Comparator.comparing(cartItemDto -> cartItemDto.getProduct().getPrice()));

        boolean drinksItems = cart.getItems().stream().filter(cartItemDto -> cartItemDto.getProduct().getType().equals(ProductType.COFFEE)).collect(Collectors.toList()).size() >= 3;
        if (drinksItems) {
            freeTopping = true;
            oneFreeTopping = orderDiscount.getTotalAmount().subtract(minPriceToppingItem.get().getProduct().getPrice());
            orderDiscount.setTotalAmount(oneFreeTopping);
            orderDiscount.setDescription("Congrats! one of your toppings has become a gift");
        }

        if (orderDiscount.getTotalAmount().compareTo(BigDecimal.valueOf(12)) >= 0) {
            discount = true;
            discount_25Percent = orderDiscount.getTotalAmount().subtract(orderDiscount.getTotalAmount().multiply(BigDecimal.valueOf(25)).divide(BigDecimal.valueOf(100)));
            orderDiscount.setTotalAmount(discount_25Percent);
            orderDiscount.setDescription("Congrats! 25% discount has appended");
        }

        if (freeTopping && discount)
            if (discount_25Percent.compareTo(oneFreeTopping) >= 0)
                orderDiscount.setTotalAmount(oneFreeTopping);
            else if (oneFreeTopping.compareTo(discount_25Percent) >= 0)
                orderDiscount.setTotalAmount(discount_25Percent);

        if (!freeTopping && !discount)
            orderDiscount.setDescription("No bonus appended!");

        return orderDiscount;
    }
}