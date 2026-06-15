package com.mall.auth.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mall.auth.dto.request.ForgotPasswordRequest;
import com.mall.auth.dto.request.LoginRequest;
import com.mall.auth.dto.request.RegisterRequest;
import com.mall.auth.dto.request.ResetPasswordRequest;
import com.mall.auth.dto.response.AuthResponse;
import com.mall.auth.dto.response.UserResponse;
import com.mall.auth.entity.Role;
import com.mall.auth.entity.User;
import com.mall.auth.exception.UnauthorizedException;
import com.mall.auth.exception.UserNotFoundException;
import com.mall.auth.repository.UserRepository;
import com.mall.auth.security.CustomUserDetails;
import com.mall.auth.security.JwtService;
import com.mall.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@Value("${auth.reset-token-expiration-minutes}")
	private long resetTokenExpirationMinutes;

	@Override
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmailIgnoreCase(request.email())) {
			throw new IllegalArgumentException("Email already registered");
		}

		User user = User.builder()
				.firstName(request.firstName())
				.lastName(request.lastName())
				.email(request.email().toLowerCase())
				.password(passwordEncoder.encode(request.password()))
				.phoneNumber(request.phoneNumber())
				.role(Role.ROLE_USER)
				.isActive(true)
				.build();

		User savedUser = userRepository.save(user);
		return buildAuthResponse(savedUser);
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		return authenticate(request, Role.ROLE_USER);
	}

	@Override
	public AuthResponse adminLogin(LoginRequest request) {
		return authenticate(request, Role.ROLE_ADMIN);
	}

	@Override
	public String validateToken(String bearerToken) {
		String token = extractToken(bearerToken);
		String email = jwtService.extractUsername(token);
		User user = findActiveUserByEmail(email);
		if (!jwtService.isTokenValid(token, new CustomUserDetails(user))) {
			throw new UnauthorizedException("Invalid or expired token");
		}
		return email;
	}

	@Override
	public String logout() {
		return "Logged out successfully";
	}

	@Override
	public String forgotPassword(ForgotPasswordRequest request) {
		User user = userRepository.findByEmailIgnoreCase(request.email())
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.email()));

		String resetToken = UUID.randomUUID().toString();
		user.setResetToken(resetToken);
		user.setResetTokenExpiresAt(Instant.now().plus(resetTokenExpirationMinutes, ChronoUnit.MINUTES));
		userRepository.save(user);
		return resetToken;
	}

	@Override
	public void resetPassword(ResetPasswordRequest request) {
		if (!request.newPassword().equals(request.confirmPassword())) {
			throw new IllegalArgumentException("Passwords do not match");
		}

		User user = userRepository.findByResetToken(request.token())
				.orElseThrow(() -> new UnauthorizedException("Invalid password reset token"));

		if (user.getResetTokenExpiresAt() == null || user.getResetTokenExpiresAt().isBefore(Instant.now())) {
			throw new UnauthorizedException("Password reset token expired");
		}

		user.setPassword(passwordEncoder.encode(request.newPassword()));
		user.setResetToken(null);
		user.setResetTokenExpiresAt(null);
		userRepository.save(user);
	}

	private AuthResponse authenticate(LoginRequest request, Role requiredRole) {
		User user = findActiveUserByEmail(request.email());
		if (user.getRole() != requiredRole) {
			throw new UnauthorizedException("Access denied for this login endpoint");
		}

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password()));

		return buildAuthResponse(user);
	}

	private User findActiveUserByEmail(String email) {
		User user = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
		if (!user.isActive()) {
			throw new UnauthorizedException("Account is blocked or inactive");
		}
		return user;
	}

	private AuthResponse buildAuthResponse(User user) {
		CustomUserDetails userDetails = new CustomUserDetails(user);
		String token = jwtService.generateToken(userDetails);
		return new AuthResponse(token, "Bearer", jwtService.getExpirationMs(), toUserResponse(user));
	}

	private String extractToken(String bearerToken) {
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new UnauthorizedException("Bearer token is missing");
		}
		return bearerToken.substring(7);
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