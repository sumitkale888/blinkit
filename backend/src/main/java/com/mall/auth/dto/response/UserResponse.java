package com.mall.auth.dto.response;

import java.time.LocalDateTime;

import com.mall.auth.entity.Role;

public record UserResponse(
		Long id,
		String firstName,
		String lastName,
		String email,
		String phoneNumber,
		Role role,
		boolean isActive,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}