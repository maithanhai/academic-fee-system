package com.mth.academicfeesystem.modules.academic.dto.response;

public record ClassEnrollmentDetailResponse(
    String className,
    String gradeLevel,
    String status
) {}
