package com.mall.product.controller;

import com.mall.product.dto.CouponDto;
import com.mall.product.dto.FestivalDiscountDto;
import com.mall.product.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    // Coupon endpoints
    @PostMapping("/coupons")
    public ResponseEntity<CouponDto> createCoupon(@RequestBody CouponDto couponDto) {
        CouponDto savedCoupon = discountService.createCoupon(couponDto);
        return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponDto>> getAllCoupons() {
        List<CouponDto> coupons = discountService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<CouponDto> getCouponById(@PathVariable Long id) {
        CouponDto coupon = discountService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<CouponDto> updateCoupon(@PathVariable Long id, @RequestBody CouponDto couponDto) {
        CouponDto updatedCoupon = discountService.updateCoupon(id, couponDto);
        return ResponseEntity.ok(updatedCoupon);
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        discountService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coupons/code/{code}")
    public ResponseEntity<CouponDto> getCouponByCode(@PathVariable String code) {
        CouponDto coupon = discountService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }


    // Festival Discount endpoints
    @PostMapping("/festivals")
    public ResponseEntity<FestivalDiscountDto> createFestivalDiscount(@RequestBody FestivalDiscountDto discountDto) {
        FestivalDiscountDto savedDiscount = discountService.createFestivalDiscount(discountDto);
        return new ResponseEntity<>(savedDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/festivals")
    public ResponseEntity<List<FestivalDiscountDto>> getAllFestivalDiscounts() {
        List<FestivalDiscountDto> discounts = discountService.getAllFestivalDiscounts();
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/festivals/{id}")
    public ResponseEntity<FestivalDiscountDto> getFestivalDiscountById(@PathVariable Long id) {
        FestivalDiscountDto discount = discountService.getFestivalDiscountById(id);
        return ResponseEntity.ok(discount);
    }

    @PutMapping("/festivals/{id}")
    public ResponseEntity<FestivalDiscountDto> updateFestivalDiscount(@PathVariable Long id, @RequestBody FestivalDiscountDto discountDto) {
        FestivalDiscountDto updatedDiscount = discountService.updateFestivalDiscount(id, discountDto);
        return ResponseEntity.ok(updatedDiscount);
    }

    @DeleteMapping("/festivals/{id}")
    public ResponseEntity<Void> deleteFestivalDiscount(@PathVariable Long id) {
        discountService.deleteFestivalDiscount(id);
        return ResponseEntity.noContent().build();
    }
}
