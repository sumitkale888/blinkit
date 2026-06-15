package com.mall.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mall.product.entity.FestivalDiscount;

public interface FestivalDiscountRepository extends JpaRepository<FestivalDiscount, Long> {
	Optional<FestivalDiscount> findByCodeIgnoreCase(String code);
}