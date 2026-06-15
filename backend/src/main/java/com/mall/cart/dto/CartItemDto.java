package com.mall.cart.dto;

public record CartItemDto(Long id, Long productId, String productName, Double productPrice, Integer quantity, Integer availableStock, Double subtotal) {
}
