package com.example.demo.dto;

import com.example.demo.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private int id;

    @NotNull(message = "name is mandatory")
    @Length(min = 3, max = 15, message = "name length should be between 3 to 15")
    private String name;

    @NotNull(message = "type is mandatory")
    @Size(min = 1, max = 1, message = "type can be either 0, 1 or 2")
    private ProductType type;

    @NotNull(message = "price is mandatory")
    private BigDecimal price;
}