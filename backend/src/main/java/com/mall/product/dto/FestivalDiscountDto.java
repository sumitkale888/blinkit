package com.mall.product.dto;

import com.mall.product.entity.DiscountType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FestivalDiscountDto {
    private Long id;
    private String name;
    private DiscountType discountType;
    private Double discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
}
