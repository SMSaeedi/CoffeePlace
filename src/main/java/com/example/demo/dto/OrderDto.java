package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private List<OrderItemDto> orderItems;
    private BigDecimal totalAmount;
    private String description;

    public BigDecimal getTotalAmount() {
        return orderItems.stream().map(orderItemDto -> (orderItemDto.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItemDto.getQuantity()))))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}