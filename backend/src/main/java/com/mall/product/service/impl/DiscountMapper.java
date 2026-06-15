package com.mall.product.service.impl;

import com.mall.product.dto.CouponDto;
import com.mall.product.dto.FestivalDiscountDto;
import com.mall.product.entity.Coupon;
import com.mall.product.entity.FestivalDiscount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountMapper {

    public CouponDto toDto(Coupon coupon) {
        CouponDto dto = new CouponDto();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDiscountType(coupon.getDiscountType());
        dto.setDiscountValue(coupon.getDiscountValue() != null ? coupon.getDiscountValue().doubleValue() : null);
        dto.setValidFrom(coupon.getValidFrom() != null ? coupon.getValidFrom().toLocalDate() : null);
        dto.setValidUntil(coupon.getValidUntil() != null ? coupon.getValidUntil().toLocalDate() : null);
        dto.setUsageLimit(coupon.getUsageLimit());
        dto.setUsageCount(coupon.getUsedCount());
        return dto;
    }

    public Coupon toEntity(CouponDto dto) {
        Coupon coupon = new Coupon();
        coupon.setId(dto.getId());
        coupon.setCode(dto.getCode());
        coupon.setDiscountType(dto.getDiscountType());
        coupon.setDiscountValue(dto.getDiscountValue() != null ? BigDecimal.valueOf(dto.getDiscountValue()) : null);
        coupon.setValidFrom(dto.getValidFrom() != null ? dto.getValidFrom().atStartOfDay() : null);
        coupon.setValidUntil(dto.getValidUntil() != null ? dto.getValidUntil().atStartOfDay() : null);
        coupon.setUsageLimit(dto.getUsageLimit());
        coupon.setUsedCount(dto.getUsageCount());
        return coupon;
    }

    public FestivalDiscountDto toDto(FestivalDiscount discount) {
        FestivalDiscountDto dto = new FestivalDiscountDto();
        dto.setId(discount.getId());
        dto.setName(discount.getName());
        dto.setDiscountType(discount.getDiscountType());
        dto.setDiscountValue(discount.getDiscountValue() != null ? discount.getDiscountValue().doubleValue() : null);
        dto.setStartDate(discount.getValidFrom() != null ? discount.getValidFrom().toLocalDate() : null);
        dto.setEndDate(discount.getValidUntil() != null ? discount.getValidUntil().toLocalDate() : null);
        return dto;
    }

    public FestivalDiscount toEntity(FestivalDiscountDto dto) {
        FestivalDiscount discount = new FestivalDiscount();
        discount.setId(dto.getId());
        discount.setName(dto.getName());
        discount.setDiscountType(dto.getDiscountType());
        discount.setDiscountValue(dto.getDiscountValue() != null ? BigDecimal.valueOf(dto.getDiscountValue()) : null);
        discount.setValidFrom(dto.getStartDate() != null ? dto.getStartDate().atStartOfDay() : null);
        discount.setValidUntil(dto.getEndDate() != null ? dto.getEndDate().atStartOfDay() : null);
        return discount;
    }
}
