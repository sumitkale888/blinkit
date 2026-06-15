package com.mall.cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mall.auth.security.CustomUserDetails;
import com.mall.cart.dto.AddCartItemRequest;
import com.mall.cart.dto.CartItemDto;
import com.mall.cart.dto.UpdateCartItemRequest;
import com.mall.cart.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartItemDto> addItemToCart(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                     @Valid @RequestBody AddCartItemRequest request) {
        CartItemDto dto = cartService.addItemToCart(currentUser.getEmail(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@AuthenticationPrincipal CustomUserDetails currentUser,
                                            @PathVariable Long cartItemId,
                                            @Valid @RequestBody UpdateCartItemRequest request) {
        CartItemDto dto = cartService.updateCartItemQuantity(currentUser.getEmail(), cartItemId, request);
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@AuthenticationPrincipal CustomUserDetails currentUser,
                                               @PathVariable Long cartItemId) {
        cartService.removeCartItem(currentUser.getEmail(), cartItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(cartService.getCart(currentUser.getEmail()));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal CustomUserDetails currentUser) {
        cartService.clearCart(currentUser.getEmail());
        return ResponseEntity.noContent().build();
    }
}
