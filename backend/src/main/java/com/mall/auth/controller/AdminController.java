package com.mall.auth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mall.auth.dto.request.UpdateRoleRequest;
import com.mall.auth.dto.response.ApiResponse;
import com.mall.auth.dto.response.UserResponse;
import com.mall.auth.security.CustomUserDetails;
import com.mall.auth.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Validated
public class AdminController {
	private final UserService userService;

	@GetMapping("/users")
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(@AuthenticationPrincipal CustomUserDetails currentUser) {
		return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", userService.getAllUsers()));
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PutMapping("/users/{id}/block")
	public ResponseEntity<UserResponse> blockUser(@PathVariable Long id) {
		return ResponseEntity.ok(userService.blockUser(id));
	}

	@PutMapping("/users/{id}/unblock")
	public ResponseEntity<UserResponse> unblockUser(@PathVariable Long id) {
		return ResponseEntity.ok(userService.unblockUser(id));
	}

	@PutMapping("/users/{id}/role")
	public ResponseEntity<UserResponse> updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
		return ResponseEntity.ok(userService.updateRole(id, request));
	}
}