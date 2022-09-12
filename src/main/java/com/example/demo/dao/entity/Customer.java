package com.example.demo.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "fullName is mandatory")
    @Length(min = 3, max = 15)
    private String name;

    @NotEmpty(message = "email is mandatory")
    @Email(message = "email must be a valid email address")
    private String email;

    @NotEmpty(message = "postAddress is mandatory")
    @Length(min = 15, max = 350)
    private String postalAddress;
}