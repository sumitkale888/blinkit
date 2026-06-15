package com.mall.auth.service;

import com.mall.auth.dto.request.ForgotPasswordRequest;
import com.mall.auth.dto.request.LoginRequest;
import com.mall.auth.dto.request.RegisterRequest;
import com.mall.auth.dto.request.ResetPasswordRequest;
import com.mall.auth.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);

	AuthResponse adminLogin(LoginRequest request);

	String validateToken(String bearerToken);

	String logout();

	String forgotPassword(ForgotPasswordRequest request);

	void resetPassword(ResetPasswordRequest request);
}