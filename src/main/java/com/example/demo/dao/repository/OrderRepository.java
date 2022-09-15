package com.example.demo.dao.repository;

import com.example.demo.dao.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
    Optional<CustomerOrder> findByCustomerId(Integer token);
}