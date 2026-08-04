package com.mth.academicfeesystem.modules.grade.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GradeUpdateRequest(
    @NotNull(message = "Điểm mới không được để trống")
    @Min(value = 0, message = "Điểm tối thiểu là 0")
    @Max(value = 10, message = "Điểm tối đa là 10")
    Double newScore,

    @NotBlank(message = "Bắt buộc phải nhập lý do sửa điểm")
    String reason
) {
    
}
