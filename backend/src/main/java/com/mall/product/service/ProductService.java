package com.mall.product.service;

import com.mall.product.dto.ProductDto;
import com.mall.product.dto.ReviewDto;
import com.mall.product.dto.ReviewRequest;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    ProductDto updateStock(Long id, Integer quantity);
    List<ProductDto> getProductsByCategory(Long categoryId);
    List<ProductDto> searchProducts(String name, Long categoryId, String brand, Double minPrice, Double maxPrice);
    ProductDto addProductImages(Long id, List<String> imageUrls);
    ProductDto removeProductImage(Long productId, Long imageId);
    ReviewDto addReview(Long productId, ReviewRequest request);
    ReviewDto updateReview(Long productId, Long reviewId, ReviewRequest request);
    void deleteReview(Long productId, Long reviewId);
    List<ReviewDto> getReviewsByProduct(Long productId);
}
