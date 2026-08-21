package com.mth.academicfeesystem.modules.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record LoginResponse(
    @JsonIgnore
    String accessToken,
    @JsonIgnore
    String refreshToken,
    @JsonIgnore
    String tokenType,
    Long userId,
    String username,
    String fullname,
    String role
) {} 