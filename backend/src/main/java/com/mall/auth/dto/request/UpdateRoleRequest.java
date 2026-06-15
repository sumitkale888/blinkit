package com.mall.auth.dto.request;

import com.mall.auth.entity.Role;

import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(@NotNull Role role) {
}