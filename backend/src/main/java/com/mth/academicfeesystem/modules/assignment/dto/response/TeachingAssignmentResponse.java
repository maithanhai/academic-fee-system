package com.mth.academicfeesystem.modules.assignment.dto.response;

public record TeachingAssignmentResponse(
    Long classId,
    Long subjectId,
    Long teacherId,
    String teacherName
) {
} 