package com.example.demo.dao.entity;

import com.example.demo.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotEmpty(message = "name is not filled")
    @Length(min = 3, max = 20)
    private String name;

    @NotEmpty(message = "type is not filled")
    private ProductType type;

    @NotEmpty(message = "price is not filled")
    private BigDecimal price;
}