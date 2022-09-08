package com.example.demo.dao.repository;

import com.example.demo.dao.entity.Product;
import com.example.demo.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByType(ProductType type);
}