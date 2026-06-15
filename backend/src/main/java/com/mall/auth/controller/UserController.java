package com.mall.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mall.auth.dto.request.ChangePasswordRequest;
import com.mall.auth.dto.request.UpdateProfileRequest;
import com.mall.auth.dto.response.ApiResponse;
import com.mall.auth.dto.response.UserResponse;
import com.mall.auth.security.CustomUserDetails;
import com.mall.auth.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
public class UserController {
	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
		return ResponseEntity.ok(userService.getCurrentUser(currentUser.getEmail()));
	}

	@PutMapping("/me")
	public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal CustomUserDetails currentUser, @Valid @RequestBody UpdateProfileRequest request) {
		return ResponseEntity.ok(userService.updateProfile(currentUser.getEmail(), request));
	}

	@PutMapping("/change-password")
	public ResponseEntity<ApiResponse<Void>> changePassword(@AuthenticationPrincipal CustomUserDetails currentUser, @Valid @RequestBody ChangePasswordRequest request) {
		userService.changePassword(currentUser.getEmail(), request);
		return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
	}

	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteAccount(@AuthenticationPrincipal CustomUserDetails currentUser) {
		userService.deleteAccount(currentUser.getEmail());
		return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
	}
}