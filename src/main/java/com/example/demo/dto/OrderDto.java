package com.example.demo.dto;

import com.example.demo.dao.entity.Cart;
import com.example.demo.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
public class OrderDto {
    private Integer id;
    private Cart cart;
    private OrderStatus orderStatus;
}