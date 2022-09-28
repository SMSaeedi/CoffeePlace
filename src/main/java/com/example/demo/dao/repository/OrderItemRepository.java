package com.example.demo.dao.repository;

import com.example.demo.dao.entity.OrderItem;
import com.example.demo.dao.entity.Product;
import com.example.demo.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<Product> findAllByProduct();
}