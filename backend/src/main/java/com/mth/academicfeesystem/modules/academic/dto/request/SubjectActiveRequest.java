package com.mth.academicfeesystem.modules.academic.dto.request;

import jakarta.validation.constraints.NotNull;

public record SubjectActiveRequest(
    @NotNull(message = "Active status is required")
    Boolean active
) {

}
