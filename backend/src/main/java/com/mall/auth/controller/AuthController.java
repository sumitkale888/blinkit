package com.mall.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mall.auth.dto.request.ForgotPasswordRequest;
import com.mall.auth.dto.request.LoginRequest;
import com.mall.auth.dto.request.RegisterRequest;
import com.mall.auth.dto.request.ResetPasswordRequest;
import com.mall.auth.dto.response.ApiResponse;
import com.mall.auth.dto.response.AuthResponse;
import com.mall.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Validated
public class AuthController {
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/admin/login")
	public ResponseEntity<AuthResponse> adminLogin(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.adminLogin(request));
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<String>> logout() {
		return ResponseEntity.ok(ApiResponse.success("Logged out successfully", authService.logout()));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		String resetToken = authService.forgotPassword(request);
		return ResponseEntity.ok(ApiResponse.success("Password reset token generated", resetToken));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		authService.resetPassword(request);
		return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
	}

	@PostMapping("/validate-token")
	public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		String email = authService.validateToken(authorizationHeader);
		return ResponseEntity.ok(ApiResponse.success("Token is valid", email));
	}
}