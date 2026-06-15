package com.mall.product.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private boolean active = true;
}
