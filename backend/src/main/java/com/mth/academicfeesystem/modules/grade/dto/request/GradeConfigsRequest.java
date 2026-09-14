package com.mth.academicfeesystem.modules.grade.dto.request;

import com.mth.academicfeesystem.common.enums.ExamType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GradeConfigsRequest(
        @NotNull(message = "ID môn học không được để trống")
        Long subjectId,

        @NotNull(message = "Cấu hình điểm miệng không được để trống")
        ConfigDetailRequest oralExamConfig,      // Miệng

        @NotNull(message = "Cấu hình điểm 15 phút không được để trống")
        ConfigDetailRequest quizExamConfig,      // 15 phút

        @NotNull(message = "Cấu hình điểm giữa kỳ không được để trống")
        ConfigDetailRequest midtermExamConfig,   // Giữa kỳ / 1 tiết

        @NotNull(message = "Cấu hình điểm cuối kỳ không được để trống")
        ConfigDetailRequest finalExamConfig) {   // Cuối kỳ 
    @Builder
    public record ConfigDetailRequest(
            @NotNull(message = "Hệ số không được để trống")
            @Min(value = 1, message = "Hệ số tối thiểu là 1")
            Integer coefficient,

            @NotNull(message = "Số cột tối đa không được để trống")
            @Min(value = 1, message = "Cần ít nhất 1 cột điểm")
            Integer maxColumn) {
    }
}
