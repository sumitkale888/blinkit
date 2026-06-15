package com.mall.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mall.product.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	Optional<Coupon> findByCode(String code);
}