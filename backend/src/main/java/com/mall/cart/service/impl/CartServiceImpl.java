package com.mall.cart.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mall.auth.entity.User;
import com.mall.auth.exception.UserNotFoundException;
import com.mall.auth.repository.UserRepository;
import com.mall.cart.dto.AddCartItemRequest;
import com.mall.cart.dto.CartItemDto;
import com.mall.cart.dto.UpdateCartItemRequest;
import com.mall.cart.entity.CartItem;
import com.mall.cart.exception.CartItemNotFoundException;
import com.mall.cart.exception.InsufficientStockException;
import com.mall.cart.repository.CartItemRepository;
import com.mall.cart.service.CartService;
import com.mall.product.entity.Product;
import com.mall.product.exception.ProductNotFoundException;
import com.mall.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItemDto addItemToCart(String userEmail, AddCartItemRequest request) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + request.productId()));

        if (request.quantity() > product.getStock()) {
            throw new InsufficientStockException("Not enough stock available for product " + product.getName());
        }

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setUser(user);
                    item.setProduct(product);
                    item.setQuantity(0);
                    return item;
                });

        int newQuantity = cartItem.getQuantity() + request.quantity();
        if (newQuantity > product.getStock()) {
            throw new InsufficientStockException("Not enough stock available for product " + product.getName());
        }

        cartItem.setQuantity(newQuantity);
        return toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto updateCartItemQuantity(String userEmail, Long cartItemId, UpdateCartItemRequest request) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CartItem cartItem = cartItemRepository.findByIdAndUser(cartItemId, user)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        Product product = cartItem.getProduct();
        if (request.quantity() < 0) {
            throw new IllegalArgumentException("Quantity must be 0 or greater");
        }
        if (request.quantity() > product.getStock()) {
            throw new InsufficientStockException("Not enough stock available for product " + product.getName());
        }

        if (request.quantity() == 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(request.quantity());
        return toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void removeCartItem(String userEmail, Long cartItemId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        CartItem cartItem = cartItemRepository.findByIdAndUser(cartItemId, user)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemDto> getCart(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return cartItemRepository.findByUser(user).stream().map(this::toDto).toList();
    }

    @Override
    public void clearCart(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        cartItemRepository.deleteByUser(user);
    }

    private CartItemDto toDto(CartItem item) {
        Product product = item.getProduct();
        return new CartItemDto(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                item.getQuantity(),
                product.getStock(),
                product.getPrice() * item.getQuantity());
    }
}
