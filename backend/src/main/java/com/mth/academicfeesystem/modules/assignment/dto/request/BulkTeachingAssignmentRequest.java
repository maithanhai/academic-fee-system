package com.mth.academicfeesystem.modules.assignment.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BulkTeachingAssignmentRequest(
    Long academicYearId,
    @NotEmpty(message = "Danh sách không được trống")
    List<TeachingAssignmentRequest> assignments
) {
    public record TeachingAssignmentRequest(
    @NotNull(message = "ID lớp học không được trống")
    Long classId,
    @NotNull(message = "ID môn học không được trống")
    Long subjectId,
    Long teacherId,
    String teacherName
) {
}
}
