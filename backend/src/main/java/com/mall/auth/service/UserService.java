package com.mall.auth.service;

import java.util.List;

import com.mall.auth.dto.request.ChangePasswordRequest;
import com.mall.auth.dto.request.UpdateProfileRequest;
import com.mall.auth.dto.request.UpdateRoleRequest;
import com.mall.auth.dto.response.UserResponse;

public interface UserService {
	UserResponse getCurrentUser(String email);

	UserResponse updateProfile(String email, UpdateProfileRequest request);

	void changePassword(String email, ChangePasswordRequest request);

	void deleteAccount(String email);

	List<UserResponse> getAllUsers();

	UserResponse getUserById(Long id);

	UserResponse blockUser(Long id);

	UserResponse unblockUser(Long id);

	UserResponse updateRole(Long id, UpdateRoleRequest request);
}