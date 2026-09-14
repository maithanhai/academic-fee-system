package com.mth.academicfeesystem.modules.grade.dto.response;

public record GradeResponse(
    Long id,
    String studentName,
    String subject,
    String examType,
    Double scoreValue
) {}
