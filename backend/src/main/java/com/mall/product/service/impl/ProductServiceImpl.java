package com.mall.product.service.impl;

import com.mall.product.dto.ProductDto;
import com.mall.product.dto.ReviewDto;
import com.mall.product.dto.ReviewRequest;
import com.mall.product.entity.Category;
import com.mall.product.entity.Product;
import com.mall.product.entity.ProductImage;
import com.mall.product.entity.Review;
import com.mall.product.exception.CategoryNotFoundException;
import com.mall.product.exception.ProductNotFoundException;
import com.mall.product.repository.CategoryRepository;
import com.mall.product.repository.ProductRepository;
import com.mall.product.repository.ReviewRepository;
import com.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + productDto.getCategoryId()));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .peek(dto -> dto.setAverageRating(getAverageRating(dto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        ProductDto dto = productMapper.toDto(product);
        dto.setAverageRating(getAverageRating(id));
        return dto;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setBrand(productDto.getBrand());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + productDto.getCategoryId()));
            existingProduct.setCategory(category);
        }

        if (productDto.getImages() != null) {
            existingProduct.getImages().clear();
            existingProduct.getImages().addAll(productMapper.toEntityImages(productDto.getImages(), existingProduct));
        }

        Product updatedProduct = productRepository.save(existingProduct);
        ProductDto dto = productMapper.toDto(updatedProduct);
        dto.setAverageRating(getAverageRating(id));
        return dto;
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto updateStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.setStock(product.getStock() + quantity);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public ProductDto addProductImages(Long id, List<String> imageUrls) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        if (imageUrls != null) {
            imageUrls.stream()
                    .filter(url -> url != null && !url.isBlank())
                    .map(url -> {
                        ProductImage image = new ProductImage();
                        image.setImageUrl(url);
                        image.setProduct(product);
                        return image;
                    })
                    .forEach(product.getImages()::add);
        }
        Product updatedProduct = productRepository.save(product);
        ProductDto dto = productMapper.toDto(updatedProduct);
        dto.setAverageRating(getAverageRating(id));
        return dto;
    }

    @Override
    public ProductDto removeProductImage(Long productId, Long imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        boolean removed = product.getImages().removeIf(image -> image.getId() != null && image.getId().equals(imageId));
        if (!removed) {
            throw new IllegalArgumentException("Product image not found with id: " + imageId);
        }
        Product updatedProduct = productRepository.save(product);
        ProductDto dto = productMapper.toDto(updatedProduct);
        dto.setAverageRating(getAverageRating(productId));
        return dto;
    }

    @Override
    public ReviewDto addReview(Long productId, ReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setProduct(product);
        Review savedReview = reviewRepository.save(review);
        return mapReview(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long productId, Long reviewId, ReviewRequest request) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));
        if (!review.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Review does not belong to product with id: " + productId);
        }
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        Review updatedReview = reviewRepository.save(review);
        return mapReview(updatedReview);
    }

    @Override
    public void deleteReview(Long productId, Long reviewId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));
        if (!review.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Review does not belong to product with id: " + productId);
        }
        reviewRepository.delete(review);
    }

    @Override
    public java.util.List<ReviewDto> getReviewsByProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapReview)
                .collect(Collectors.toList());
    }

    private ReviewDto mapReview(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setProductId(review.getProduct() != null ? review.getProduct().getId() : null);
        return dto;
    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(productMapper::toDto)
                .peek(dto -> dto.setAverageRating(getAverageRating(dto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProducts(String name, Long categoryId, String brand, Double minPrice, Double maxPrice) {
        return productRepository.searchProducts(name, categoryId, brand, minPrice, maxPrice).stream()
                .map(productMapper::toDto)
                .peek(dto -> dto.setAverageRating(getAverageRating(dto.getId())))
                .collect(Collectors.toList());
    }

    private Double getAverageRating(Long productId) {
        return reviewRepository.findAverageRatingByProductId(productId);
    }
}
