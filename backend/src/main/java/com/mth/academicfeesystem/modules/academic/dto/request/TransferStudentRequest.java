package com.mth.academicfeesystem.modules.academic.dto.request;

import jakarta.validation.constraints.NotNull;

public record TransferStudentRequest(
    @NotNull(message = "Id student is required") Long studentId,
    @NotNull(message = "Id class is required") Long targetClassId
) {}
