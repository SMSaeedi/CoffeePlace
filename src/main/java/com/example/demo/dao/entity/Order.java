package com.example.demo.dao.entity;

import com.example.demo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "customer_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;
    @Column(unique = true, nullable = false)
    private Integer customerId;
    private Date orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private String description;
}