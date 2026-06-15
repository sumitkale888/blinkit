package com.mall.product.controller;

import com.mall.product.dto.ProductDto;
import com.mall.product.dto.ProductImageRequest;
import com.mall.product.dto.ReviewDto;
import com.mall.product.dto.ReviewRequest;
import com.mall.product.dto.UpdateStockRequest;
import com.mall.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDto> updateStock(@PathVariable Long id, @RequestBody UpdateStockRequest stockRequest) {
        ProductDto updatedProduct = productService.updateStock(id, stockRequest.getQuantity());
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ProductDto> addProductImages(@PathVariable Long id, @Valid @RequestBody ProductImageRequest request) {
        ProductDto updatedProduct = productService.addProductImages(id, request.getImageUrls());
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<ProductDto> removeProductImage(@PathVariable Long id, @PathVariable Long imageId) {
        ProductDto updatedProduct = productService.removeProductImage(id, imageId);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        ReviewDto reviewDto = productService.addReview(id, request);
        return new ResponseEntity<>(reviewDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @PathVariable Long reviewId, @Valid @RequestBody ReviewRequest request) {
        ReviewDto reviewDto = productService.updateReview(id, reviewId, request);
        return ResponseEntity.ok(reviewDto);
    }

    @DeleteMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @PathVariable Long reviewId) {
        productService.deleteReview(id, reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsByProduct(@PathVariable Long id) {
        List<ReviewDto> reviews = productService.getReviewsByProduct(id);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(productService.searchProducts(name, categoryId, brand, minPrice, maxPrice));
    }
}
