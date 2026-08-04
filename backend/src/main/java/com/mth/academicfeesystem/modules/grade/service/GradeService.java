package com.mth.academicfeesystem.modules.grade.service;

import java.util.List;

import com.mth.academicfeesystem.modules.grade.dto.request.GradeRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeDetailResponse;

public interface GradeService {
    void inputGrades(GradeRequest request);
    List<GradeDetailResponse> getSubjectGradeBoard(Long classId, Long subjectId, Long semesterId);
    List<GradeDetailResponse> getMyGrades(Long studentId, Long semesterId);
}
