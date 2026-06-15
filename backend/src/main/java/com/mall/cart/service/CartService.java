package com.mall.cart.service;

import java.util.List;

import com.mall.cart.dto.AddCartItemRequest;
import com.mall.cart.dto.CartItemDto;
import com.mall.cart.dto.UpdateCartItemRequest;

public interface CartService {
    CartItemDto addItemToCart(String userEmail, AddCartItemRequest request);
    CartItemDto updateCartItemQuantity(String userEmail, Long cartItemId, UpdateCartItemRequest request);
    void removeCartItem(String userEmail, Long cartItemId);
    List<CartItemDto> getCart(String userEmail);
    void clearCart(String userEmail);
}
