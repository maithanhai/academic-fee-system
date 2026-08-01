package com.mth.academicfeesystem.modules.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChangeActiveRequest(
    @NotNull(message = "Active is not null")
    Boolean active
) {
}
