package com.mall.order.service;

import java.util.List;

import com.mall.order.dto.OrderDto;
import com.mall.order.dto.OrderRequest;

public interface OrderService {
    OrderDto placeOrder(String userEmail, OrderRequest request);
    OrderDto getOrderDetails(String userEmail, Long orderId);
    List<OrderDto> listUserOrders(String userEmail);
    OrderDto cancelOrder(String userEmail, Long orderId);
}
