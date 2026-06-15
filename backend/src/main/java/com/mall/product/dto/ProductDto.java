package com.mall.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private Double price;
    private Integer stock;
    private Long categoryId;
    private List<ProductImageDto> images;
    private String thumbnailUrl;
    private Double averageRating;
}
