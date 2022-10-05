package com.example.demo.utils;

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

    @Value("${service.order.descTopping}")
    private static String descTopping;

    @Value("${service.order.descDiscount}")
    private static String descDiscount;

    @Value("${service.order.none}")
    private static String noBones;

    public static Order calculateBones(OrderDto orderDto) {
        boolean freeTopping = false;
        BigDecimal oneFreeTopping = BigDecimal.ZERO;
        boolean discount = false;
        BigDecimal discount_25Percent = BigDecimal.ZERO;

        List<OrderItemDto> toppingsList = orderDto.getOrderItems().stream().filter(orderDetails -> orderDetails.getProduct().getType().equals(ProductType.TOPPINGS)).collect(Collectors.toList());
        Optional<OrderItemDto> minPriceToppingItem = toppingsList.stream().min(Comparator.comparing(cartItemDto -> cartItemDto.getProduct().getPrice()));

        boolean drinksItems = orderDto.getOrderItems().stream().filter(cartItemDto -> cartItemDto.getProduct().getType().equals(ProductType.COFFEE)).collect(Collectors.toList()).size() >= 3;
        if (drinksItems) {
            freeTopping = true;
            oneFreeTopping = orderDto.getTotalAmount().subtract(minPriceToppingItem.get().getProduct().getPrice());
        }

        if (orderDto.getTotalAmount().compareTo(BigDecimal.valueOf(12)) >= 0) {
            discount = true;
            discount_25Percent = orderDto.getTotalAmount().subtract(orderDto.getTotalAmount().multiply(BigDecimal.valueOf(25)).divide(BigDecimal.valueOf(100)));
        }

        if (freeTopping && discount)
            if (discount_25Percent.compareTo(oneFreeTopping) >= 0) {
                orderDto.setTotalAmount(oneFreeTopping);
                orderDto.setDescription(descTopping);
            } else if (oneFreeTopping.compareTo(discount_25Percent) >= 0) {
                orderDto.setTotalAmount(discount_25Percent);
                orderDto.setDescription(descDiscount);
            }

        orderDto.setDescription(noBones);
        return toOrderEntity(orderDto);
    }

    private static Order toOrderEntity(OrderDto orderDto) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(orderDto, Order.class);
    }
}