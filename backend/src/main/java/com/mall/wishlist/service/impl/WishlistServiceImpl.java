package com.mall.wishlist.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mall.auth.entity.User;
import com.mall.auth.exception.UserNotFoundException;
import com.mall.auth.repository.UserRepository;
import com.mall.product.entity.Product;
import com.mall.product.exception.ProductNotFoundException;
import com.mall.product.repository.ProductRepository;
import com.mall.wishlist.dto.WishlistItemDto;
import com.mall.wishlist.entity.WishlistItem;
import com.mall.wishlist.repository.WishlistItemRepository;
import com.mall.wishlist.service.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistItemRepository wishlistItemRepository;

    @Override
    public WishlistItemDto addToWishlist(String userEmail, Long productId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        WishlistItem item = wishlistItemRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    WishlistItem newItem = new WishlistItem();
                    newItem.setUser(user);
                    newItem.setProduct(product);
                    return newItem;
                });

        return toDto(wishlistItemRepository.save(item));
    }

    @Override
    public void removeFromWishlist(String userEmail, Long productId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        wishlistItemRepository.deleteByUserAndProduct(user, product);
    }

    @Override
    public List<WishlistItemDto> getWishlist(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return wishlistItemRepository.findByUser(user).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private WishlistItemDto toDto(WishlistItem item) {
        Product product = item.getProduct();
        return new WishlistItemDto(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImages() != null && !product.getImages().isEmpty() ? product.getImages().get(0).getImageUrl() : null
        );
    }
}
