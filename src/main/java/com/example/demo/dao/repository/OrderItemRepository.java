package com.example.demo.dao.repository;

import com.example.demo.dao.entity.OrderItem;
import com.example.demo.dto.MostUsedProduct;
import com.example.demo.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
//    @Query("SELECT new com.example.demo.dto.MostUsedProduct(o.product, SUM (o.quantity)) "
//            + "FROM OrderItem o "
//            + "WHERE o.product.type =: productType "
//            + "GROUP BY o.product "
//            + "ORDER BY SUM (o.quantity) DESC")
//    List<MostUsedProduct> mostUsedProducts(ProductType productType);
}