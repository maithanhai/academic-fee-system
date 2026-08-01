package com.mth.academicfeesystem.modules.user.dto.response;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long userId,
    String username,
    String fullname,
    String role
) {} 