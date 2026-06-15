package com.mall.product.service;

import com.mall.product.dto.CouponDto;
import com.mall.product.dto.FestivalDiscountDto;

import java.util.List;

public interface DiscountService {
    // Coupon methods
    CouponDto createCoupon(CouponDto couponDto);
    List<CouponDto> getAllCoupons();
    CouponDto getCouponById(Long id);
    CouponDto updateCoupon(Long id, CouponDto couponDto);
    void deleteCoupon(Long id);
    CouponDto getCouponByCode(String code);

    // Festival Discount methods
    FestivalDiscountDto createFestivalDiscount(FestivalDiscountDto festivalDiscountDto);
    List<FestivalDiscountDto> getAllFestivalDiscounts();
    FestivalDiscountDto getFestivalDiscountById(Long id);
    FestivalDiscountDto updateFestivalDiscount(Long id, FestivalDiscountDto festivalDiscountDto);
    void deleteFestivalDiscount(Long id);
}