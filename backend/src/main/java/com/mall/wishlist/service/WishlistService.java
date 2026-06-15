package com.mall.wishlist.service;

import java.util.List;

import com.mall.wishlist.dto.WishlistItemDto;

public interface WishlistService {
    WishlistItemDto addToWishlist(String userEmail, Long productId);
    void removeFromWishlist(String userEmail, Long productId);
    List<WishlistItemDto> getWishlist(String userEmail);
}
