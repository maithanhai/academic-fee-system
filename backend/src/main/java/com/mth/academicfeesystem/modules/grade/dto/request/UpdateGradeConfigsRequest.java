package com.mth.academicfeesystem.modules.grade.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateGradeConfigsRequest(
        @NotNull(message = "ID môn học không được trống")
        Long subjectId,
        ConfigDetailRequest oralExamConfig, // Miệng
        ConfigDetailRequest quizExamConfig, // 15 phút
        ConfigDetailRequest midtermExamConfig, // Giữa kỳ / 1 tiết
        ConfigDetailRequest finalExamConfig) { // Cuối kỳ
    public record ConfigDetailRequest(
            Integer coefficient,
            Integer maxColumn) {
    }
}
