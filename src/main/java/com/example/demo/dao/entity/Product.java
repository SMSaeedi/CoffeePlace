package com.example.demo.dao.entity;

import com.example.demo.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "name is not filled")
    private String name;

    @NotEmpty(message = "name is not filled")
    private ProductType type;

    @NotEmpty(message = "name is not filled")
    private BigDecimal price;
}