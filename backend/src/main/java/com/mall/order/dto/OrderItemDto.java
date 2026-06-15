package com.mall.order.dto;

public record OrderItemDto(Long productId, String productName, Double productPrice, Integer quantity, Double subtotal) {
}
