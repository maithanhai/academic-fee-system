package com.mth.academicfeesystem.modules.assignment.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record BulkHomeroomAssignmentRequest(
                Long academicYearId, List<HomeroomAssignmentRequest> assignments) {
        public record HomeroomAssignmentRequest(
                        @NotNull(message = "ID class not null") Long classId,
                        @NotNull(message = "ID teacher not null") Long teacherId) {
        }
}
