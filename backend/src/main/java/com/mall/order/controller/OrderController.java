package com.mall.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mall.auth.security.CustomUserDetails;
import com.mall.order.dto.OrderDto;
import com.mall.order.dto.OrderRequest;
import com.mall.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@AuthenticationPrincipal CustomUserDetails currentUser,
                                               @Valid @RequestBody OrderRequest request) {
        OrderDto dto = orderService.placeOrder(currentUser.getEmail(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetails(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                    @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(currentUser.getEmail(), orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> listOrders(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(orderService.listUserOrders(currentUser.getEmail()));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(currentUser.getEmail(), orderId));
    }
}
