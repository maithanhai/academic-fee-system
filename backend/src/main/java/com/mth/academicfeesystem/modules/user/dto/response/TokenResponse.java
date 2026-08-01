package com.mth.academicfeesystem.modules.user.dto.response;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType
) {}
