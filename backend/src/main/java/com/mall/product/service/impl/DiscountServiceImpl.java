package com.mall.product.service.impl;

import com.mall.product.dto.CouponDto;
import com.mall.product.dto.FestivalDiscountDto;
import com.mall.product.entity.Coupon;
import com.mall.product.entity.FestivalDiscount;
import com.mall.product.exception.CouponNotFoundException;
import com.mall.product.exception.FestivalDiscountNotFoundException;
import com.mall.product.repository.CouponRepository;
import com.mall.product.repository.FestivalDiscountRepository;
import com.mall.product.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final CouponRepository couponRepository;
    private final FestivalDiscountRepository festivalDiscountRepository;
    private final DiscountMapper discountMapper;

    @Override
    public CouponDto createCoupon(CouponDto couponDto) {
        Coupon coupon = discountMapper.toEntity(couponDto);
        Coupon savedCoupon = couponRepository.save(coupon);
        return discountMapper.toDto(savedCoupon);
    }

    @Override
    public List<CouponDto> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(discountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CouponDto getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
        return discountMapper.toDto(coupon);
    }

    @Override
    public CouponDto updateCoupon(Long id, CouponDto couponDto) {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
        
        existingCoupon.setCode(couponDto.getCode());
        existingCoupon.setDiscountType(couponDto.getDiscountType());
        existingCoupon.setDiscountValue(couponDto.getDiscountValue() != null ? BigDecimal.valueOf(couponDto.getDiscountValue()) : null);
        existingCoupon.setValidFrom(couponDto.getValidFrom() != null ? couponDto.getValidFrom().atStartOfDay() : null);
        existingCoupon.setValidUntil(couponDto.getValidUntil() != null ? couponDto.getValidUntil().atStartOfDay() : null);
        existingCoupon.setUsageLimit(couponDto.getUsageLimit());

        Coupon updatedCoupon = couponRepository.save(existingCoupon);
        return discountMapper.toDto(updatedCoupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new CouponNotFoundException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    @Override
    public CouponDto getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with code: " + code));
        return discountMapper.toDto(coupon);
    }

    @Override
    public FestivalDiscountDto createFestivalDiscount(FestivalDiscountDto festivalDiscountDto) {
        FestivalDiscount discount = discountMapper.toEntity(festivalDiscountDto);
        FestivalDiscount savedDiscount = festivalDiscountRepository.save(discount);
        return discountMapper.toDto(savedDiscount);
    }

    @Override
    public List<FestivalDiscountDto> getAllFestivalDiscounts() {
        return festivalDiscountRepository.findAll().stream()
                .map(discountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FestivalDiscountDto getFestivalDiscountById(Long id) {
        FestivalDiscount discount = festivalDiscountRepository.findById(id)
                .orElseThrow(() -> new FestivalDiscountNotFoundException("Festival discount not found with id: " + id));
        return discountMapper.toDto(discount);
    }

    @Override
    public FestivalDiscountDto updateFestivalDiscount(Long id, FestivalDiscountDto festivalDiscountDto) {
        FestivalDiscount existingDiscount = festivalDiscountRepository.findById(id)
                .orElseThrow(() -> new FestivalDiscountNotFoundException("Festival discount not found with id: " + id));

        existingDiscount.setName(festivalDiscountDto.getName());
        existingDiscount.setDiscountType(festivalDiscountDto.getDiscountType());
        existingDiscount.setDiscountValue(festivalDiscountDto.getDiscountValue() != null ? BigDecimal.valueOf(festivalDiscountDto.getDiscountValue()) : null);
        existingDiscount.setValidFrom(festivalDiscountDto.getStartDate() != null ? festivalDiscountDto.getStartDate().atStartOfDay() : null);
        existingDiscount.setValidUntil(festivalDiscountDto.getEndDate() != null ? festivalDiscountDto.getEndDate().atStartOfDay() : null);

        FestivalDiscount updatedDiscount = festivalDiscountRepository.save(existingDiscount);
        return discountMapper.toDto(updatedDiscount);
    }

    @Override
    public void deleteFestivalDiscount(Long id) {
        if (!festivalDiscountRepository.existsById(id)) {
            throw new FestivalDiscountNotFoundException("Festival discount not found with id: " + id);
        }
        festivalDiscountRepository.deleteById(id);
    }
}
