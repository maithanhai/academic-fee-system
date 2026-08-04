package com.mth.academicfeesystem.modules.grade.dto.response;

import com.mth.academicfeesystem.common.enums.ExamType;

public record GradeConfigDetailResponse(
    Long configId,
    ExamType examType,
    Integer coefficient,
    Integer maxColumn
) {
}