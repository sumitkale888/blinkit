package com.mall.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.mall.order.entity.OrderStatus;

public record OrderDto(Long id, Long userId, AddressSummary address, List<OrderItemDto> items, Double totalAmount, OrderStatus status, LocalDateTime orderedAt) {
    public record AddressSummary(Long id, String label, String street, String city, String state, String country, String postalCode) {
    }
}
