package com.mall.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mall.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
