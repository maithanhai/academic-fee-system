package com.mth.academicfeesystem.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
    @NotBlank @Size(min = 6)
    String newPassword,
    String oldPassword
) {}
