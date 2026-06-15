package com.mall.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coupons")
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String code;

	@Column(length = 1000)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DiscountType discountType;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal discountValue;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal minimumOrderAmount = BigDecimal.ZERO;

	@Column(precision = 19, scale = 2)
	private BigDecimal maximumDiscountAmount;

	@Column(nullable = false)
	private boolean active = true;

	private LocalDateTime validFrom;

	private LocalDateTime validUntil;

	@Column(nullable = false)
	private Integer usageLimit = 0;

	@Column(nullable = false)
	private Integer usedCount = 0;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}