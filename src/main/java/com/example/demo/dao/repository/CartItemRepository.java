package com.example.demo.dao.repository;

import com.example.demo.dao.entity.CartItem;
import com.example.demo.dao.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    void deleteByProduct(Product product);
}