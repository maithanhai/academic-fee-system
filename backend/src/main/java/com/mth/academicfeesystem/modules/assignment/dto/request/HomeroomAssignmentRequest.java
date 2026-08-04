package com.mth.academicfeesystem.modules.assignment.dto.request;

import jakarta.validation.constraints.NotNull;

public record HomeroomAssignmentRequest (
    @NotNull(message = "ID class not null")
    Long classId,
    @NotNull(message = "ID teacher not null")
    Long teacherId
){}
