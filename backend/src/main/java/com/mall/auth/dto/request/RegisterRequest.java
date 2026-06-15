package com.mall.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
		@NotBlank @Size(max = 50) String firstName,
		@NotBlank @Size(max = 50) String lastName,
		@NotBlank @Email @Size(max = 100) String email,
		@NotBlank @Size(min = 8, max = 100) String password,
		@Pattern(regexp = "^$|^[0-9+() -]{7,20}$", message = "phoneNumber must contain only digits, spaces, +, -, and ()") String phoneNumber) {
}