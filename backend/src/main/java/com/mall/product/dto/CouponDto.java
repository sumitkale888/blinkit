package com.mall.product.dto;

import com.mall.product.entity.DiscountType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponDto {
    private Long id;
    private String code;
    private DiscountType discountType;
    private Double discountValue;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private int usageLimit;
    private int usageCount;
}
