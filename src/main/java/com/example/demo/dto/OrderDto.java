package com.example.demo.dto;

import com.example.demo.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private List<OrderItemDto> orderDetails;
    private Date orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private String description;

    public BigDecimal getTotalAmount() {
        return orderDetails.stream().map(orderItemDto -> (orderItemDto.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItemDto.getQuantity())))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}