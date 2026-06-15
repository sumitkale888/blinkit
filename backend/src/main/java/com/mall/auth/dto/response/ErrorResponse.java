package com.mall.auth.dto.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path,
		List<String> details) {
}