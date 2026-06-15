package com.mall.product.service.impl;

import com.mall.product.dto.ProductDto;
import com.mall.product.dto.ProductImageDto;
import com.mall.product.entity.Product;
import com.mall.product.entity.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }
        if (product.getImages() != null) {
            dto.setImages(product.getImages().stream().map(this::toDto).collect(Collectors.toList()));
            if (!dto.getImages().isEmpty()) {
                dto.setThumbnailUrl(dto.getImages().get(0).getImageUrl());
            }
        }
        return dto;
    }

    public Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        if (dto.getImages() != null) {
            product.setImages(toEntityImages(dto.getImages(), product));
        }
        // Category will be set in the service
        return product;
    }

    public java.util.List<ProductImage> toEntityImages(java.util.List<ProductImageDto> imageDtos, Product product) {
        return imageDtos == null ? java.util.List.of() : imageDtos.stream()
                .map(dto -> {
                    ProductImage image = toEntity(dto);
                    image.setProduct(product);
                    return image;
                })
                .collect(Collectors.toList());
    }

    public ProductImageDto toDto(ProductImage productImage) {
        ProductImageDto dto = new ProductImageDto();
        dto.setId(productImage.getId());
        dto.setImageUrl(productImage.getImageUrl());
        return dto;
    }

    public ProductImage toEntity(ProductImageDto dto) {
        ProductImage image = new ProductImage();
        image.setId(dto.getId());
        image.setImageUrl(dto.getImageUrl());
        return image;
    }
}
