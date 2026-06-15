package com.mall.order.dto;

import jakarta.validation.constraints.NotNull;

import com.mall.order.entity.OrderStatus;

public record OrderStatusUpdateRequest(@NotNull OrderStatus status) {
}
