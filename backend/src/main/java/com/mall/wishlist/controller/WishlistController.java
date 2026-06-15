package com.mall.wishlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.mall.auth.security.CustomUserDetails;
import com.mall.wishlist.dto.WishlistItemDto;
import com.mall.wishlist.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{productId}")
    public ResponseEntity<WishlistItemDto> addToWishlist(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                         @PathVariable Long productId) {
        WishlistItemDto dto = wishlistService.addToWishlist(currentUser.getEmail(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                   @PathVariable Long productId) {
        wishlistService.removeFromWishlist(currentUser.getEmail(), productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemDto>> getWishlist(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(wishlistService.getWishlist(currentUser.getEmail()));
    }
}
