package com.mth.academicfeesystem.modules.academic.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
    @NotNull(message = "ID lớp học không được để trống")
    Long classId,
    @NotEmpty(message = "Danh sách ID học sinh không được để trống")
    List<Long> studentIds
) {

}
