package com.mall.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mall.product.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findBySlugIgnoreCase(String slug);
}