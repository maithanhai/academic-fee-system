package com.mth.academicfeesystem.modules.grade.dto.request;

import com.mth.academicfeesystem.common.enums.ExamType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GradeSaveRequest(
                @NotNull Long semesterId,
                @NotNull Long studentId,
                @NotNull ExamType examType,
                @NotNull Integer ordinalNumber,
                @NotNull @Min(value = 0, message = "Điểm không được nhỏ hơn 0") @Max(value = 10, message = "Điểm không được lớn hơn 10") Double score) {
}
