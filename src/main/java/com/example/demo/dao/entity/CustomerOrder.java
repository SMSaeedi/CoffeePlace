package com.example.demo.dao.entity;

import com.example.demo.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customerOrder", uniqueConstraints = {@UniqueConstraint(columnNames = "customerId")})
public class CustomerOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany
    private List<OrderItem> orderItems;
    private Integer customerId;
    private Date orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
}