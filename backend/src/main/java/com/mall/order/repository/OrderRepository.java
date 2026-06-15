package com.mall.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mall.auth.entity.User;
import com.mall.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderedAtDesc(User user);
    boolean existsByIdAndUser(Long id, User user);
}
