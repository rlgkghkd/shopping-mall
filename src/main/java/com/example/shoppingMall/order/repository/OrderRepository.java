package com.example.shoppingMall.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoppingMall.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
