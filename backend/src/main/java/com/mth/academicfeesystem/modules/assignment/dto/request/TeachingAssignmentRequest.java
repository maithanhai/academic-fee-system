package com.mth.academicfeesystem.modules.assignment.dto.request;

import jakarta.validation.constraints.NotNull;

public record TeachingAssignmentRequest(
    @NotNull(message = "ID lớp học không được trống")
    Long classId,
    @NotNull(message = "ID môn học không được trống")
    Long subjectId,
    Long teacherId,
    String teacherName
) {
}