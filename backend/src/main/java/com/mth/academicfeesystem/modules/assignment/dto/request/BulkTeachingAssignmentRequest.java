package com.mth.academicfeesystem.modules.assignment.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record BulkTeachingAssignmentRequest(
    @NotEmpty(message = "Danh sách không được trống")
    List<TeachingAssignmentRequest> assignments
) {

}
