package com.mth.academicfeesystem.modules.academic.dto.request;

import jakarta.validation.constraints.NotNull;

public record TransferRequest(
    @NotNull(message = "ID ghi danh không được để trống")
    Long enrollmentId,
    @NotNull(message = "ID lớp học mới không được để trống")
    Long newClassId
) {}
