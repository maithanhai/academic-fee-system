package com.mth.academicfeesystem.modules.grade.dto.response;

import com.mth.academicfeesystem.common.enums.ExamType;

import lombok.Builder;

@Builder
public record GradeDetailResponse(
    Long gradeId,
    Long studentId,
    String studentName,
    String subjectName,
    ExamType examType,
    Integer ordinalNumber,
    Double scoreValue
) {}
