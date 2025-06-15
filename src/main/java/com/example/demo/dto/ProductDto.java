package com.example.demo.dto;

import com.example.demo.enums.ProductType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @NotNull(message = "name is mandatory")
    @Length(min = 3, max = 15, message = "name length should be between 3 to 15")
    private String name;

    @NotNull(message = "type is mandatory")
    @Size(min = 1, max = 1, message = "type can be either 0, 1 or 2")
    private ProductType type;

    @NotNull(message = "price is mandatory")
    private BigDecimal price;
}