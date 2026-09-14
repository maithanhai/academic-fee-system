package com.mth.academicfeesystem.modules.grade.dto.response;

import java.util.List;
import java.util.Map;

import lombok.Builder;

@Builder
public record GradeTableResponse(
        Long classId,
        Long subjectId,
        Long semesterId,
        List<GradeColumnConfig> columns,
        List<StudentGradeRow> rows) {
    @Builder
    public record GradeColumnConfig(
            String examType,
            Integer coefficient,
            Integer maxColumn) {
    }

    @Builder
    public record StudentGradeRow(
            Long studentId,
            String studentName,
            String gender,
            Map<String, List<GradeDetailResponse>> scores) {
        @Builder
        public record GradeDetailResponse(
                Long gradeId,
                Double score,
                Integer ordinalNumber) {
        }
    }
}
