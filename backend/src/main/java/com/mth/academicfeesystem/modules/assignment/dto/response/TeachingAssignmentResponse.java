package com.mth.academicfeesystem.modules.assignment.dto.response;

import lombok.Builder;

@Builder
public record TeachingAssignmentResponse(
    Long classId,
    Long subjectId,
    Long teacherId,
    String teacherName,
    Long workload
) {
} 