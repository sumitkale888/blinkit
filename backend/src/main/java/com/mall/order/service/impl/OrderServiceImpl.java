package com.mall.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mall.address.entity.Address;
import com.mall.address.repository.AddressRepository;
import com.mall.auth.entity.User;
import com.mall.auth.exception.UserNotFoundException;
import com.mall.auth.repository.UserRepository;
import com.mall.cart.entity.CartItem;
import com.mall.cart.repository.CartItemRepository;
import com.mall.order.dto.OrderDto;
import com.mall.order.dto.OrderRequest;
import com.mall.order.dto.OrderItemDto;
import com.mall.order.entity.Order;
import com.mall.order.entity.OrderItem;
import com.mall.order.entity.OrderStatus;
import com.mall.order.repository.OrderRepository;
import com.mall.order.service.OrderService;
import com.mall.product.entity.Product;
import com.mall.product.exception.ProductNotFoundException;
import com.mall.product.repository.ProductRepository;
import com.mall.address.exception.AddressNotFoundException;
import com.mall.cart.exception.InsufficientStockException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDto placeOrder(String userEmail, OrderRequest request) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepository.findByIdAndUser(request.addressId(), user)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PENDING);

        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            if (cartItem.getQuantity() > product.getStock()) {
                throw new InsufficientStockException("Not enough stock for " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice() * cartItem.getQuantity());
            orderItems.add(orderItem);
            total += orderItem.getSubtotal();
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
        return toDto(savedOrder);
    }

    @Override
    public OrderDto getOrderDetails(String userEmail, Long orderId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Order does not belong to user");
        }

        return toDto(order);
    }

    @Override
    public List<OrderDto> listUserOrders(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return orderRepository.findByUserOrderByOrderedAtDesc(user).stream().map(this::toDto).toList();
    }

    @Override
    public OrderDto cancelOrder(String userEmail, Long orderId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Order does not belong to user");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return toDto(order);
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only pending or confirmed orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setTotalAmount(order.getTotalAmount());

        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        });

        return toDto(orderRepository.save(order));
    }

    private OrderDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getSubtotal()))
                .toList();

        return new OrderDto(
                order.getId(),
                order.getUser().getId(),
                new OrderDto.AddressSummary(
                        order.getAddress().getId(),
                        order.getAddress().getLabel(),
                        order.getAddress().getStreet(),
                        order.getAddress().getCity(),
                        order.getAddress().getState(),
                        order.getAddress().getCountry(),
                        order.getAddress().getPostalCode()),
                items,
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderedAt());
    }
}
