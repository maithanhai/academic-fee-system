package com.mth.academicfeesystem.modules.grade.dto.response;

import java.util.List;

import com.mth.academicfeesystem.common.enums.ExamType;

import lombok.Builder;

@Builder
public record StudentTranscriptResponse(
        Long studentId,
        String fullName,
        List<SemesterTranscript> semesters) {
    @Builder
    public record SemesterTranscript(
            Long semesterId, String semesterName, List<SubjectScore> subjectScores) {
    }

    @Builder
    public record SubjectScore(
            Long subjectId, String subjectName, List<ExamGroup> examGroups) {
    }

    @Builder
    public record ExamGroup(
            ExamType examType, List<GradeDetail> scores) {
    }

    @Builder
    public record GradeDetail(
            Long gradeId, double scoreValue) {
    }
}