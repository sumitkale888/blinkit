package com.mall.product.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class ProductImageRequest {
    @NotEmpty(message = "At least one image URL is required")
    private List<String> imageUrls;

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
