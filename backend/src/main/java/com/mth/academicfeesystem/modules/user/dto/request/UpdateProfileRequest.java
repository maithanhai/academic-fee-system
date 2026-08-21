package com.mth.academicfeesystem.modules.user.dto.request;

public record UpdateProfileRequest(
    String phone,
    String email,
    String address,
    String phoneParent
) {}
