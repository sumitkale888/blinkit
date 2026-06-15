package com.mall.auth.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mall.auth.dto.request.ChangePasswordRequest;
import com.mall.auth.dto.request.UpdateProfileRequest;
import com.mall.auth.dto.request.UpdateRoleRequest;
import com.mall.auth.dto.response.UserResponse;
import com.mall.auth.entity.User;
import com.mall.auth.exception.UnauthorizedException;
import com.mall.auth.exception.UserNotFoundException;
import com.mall.auth.repository.UserRepository;
import com.mall.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public UserResponse getCurrentUser(String email) {
		return toUserResponse(findUserByEmail(email));
	}

	@Override
	public UserResponse updateProfile(String email, UpdateProfileRequest request) {
		User user = findUserByEmail(email);
		if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmailIgnoreCase(request.email())) {
			throw new IllegalArgumentException("Email already registered");
		}

		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email().toLowerCase());
		user.setPhoneNumber(request.phoneNumber());
		return toUserResponse(userRepository.save(user));
	}

	@Override
	public void changePassword(String email, ChangePasswordRequest request) {
		if (!request.newPassword().equals(request.confirmPassword())) {
			throw new IllegalArgumentException("Passwords do not match");
		}

		User user = findUserByEmail(email);
		if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
			throw new UnauthorizedException("Current password is incorrect");
		}

		user.setPassword(passwordEncoder.encode(request.newPassword()));
		userRepository.save(user);
	}

	@Override
	public void deleteAccount(String email) {
		User user = findUserByEmail(email);
		userRepository.delete(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::toUserResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getUserById(Long id) {
		return toUserResponse(findUserById(id));
	}

	@Override
	public UserResponse blockUser(Long id) {
		User user = findUserById(id);
		user.setActive(false);
		return toUserResponse(userRepository.save(user));
	}

	@Override
	public UserResponse unblockUser(Long id) {
		User user = findUserById(id);
		user.setActive(true);
		return toUserResponse(userRepository.save(user));
	}

	@Override
	public UserResponse updateRole(Long id, UpdateRoleRequest request) {
		User user = findUserById(id);
		user.setRole(request.role());
		return toUserResponse(userRepository.save(user));
	}

	private User findUserByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
	}

	private User findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
	}

	private UserResponse toUserResponse(User user) {
		return new UserResponse(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.getRole(),
				user.isActive(),
				user.getCreatedAt(),
				user.getUpdatedAt());
	}
}