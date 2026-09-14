package com.mth.academicfeesystem.modules.grade.dto.response;

import lombok.Builder;

@Builder
public record GradeConfigsResponse(
        Long subjectId,
        String subjectName,
        ConfigDetailResponse oralExamConfig, // Miệng
        ConfigDetailResponse quizExamConfig, // 15 phút
        ConfigDetailResponse midtermExamConfig, // Giữa kỳ / 1 tiết
        ConfigDetailResponse finalExamConfig) { // Cuối kỳ
    @Builder
    public record ConfigDetailResponse(
            Integer coefficient,
            Integer maxColumn) {
    }
}
