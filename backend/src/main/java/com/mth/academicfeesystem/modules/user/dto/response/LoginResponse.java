package com.mth.academicfeesystem.modules.user.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long userId,
    String username,
    String fullname,
    String role
) {} 