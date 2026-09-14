package com.mth.academicfeesystem.modules.grade.service;

import com.mth.academicfeesystem.modules.grade.dto.request.GradeSaveRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse.StudentGradeRow.GradeDetailResponse;
import com.mth.academicfeesystem.security.CustomUserPrincipal;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse;

public interface GradeService {

    StudentTranscriptResponse getTranscriptByAdmin(Long studentId, Long classId);

    StudentTranscriptResponse getTranscriptByTeacher(Long studentId, Long classId);

    StudentTranscriptResponse getTranscriptByStudent(CustomUserPrincipal principal, Long academicYearId);

    GradeTableResponse getGradeTable(Long classId, Long subjectId, Long semesterId);

    GradeDetailResponse autoSaveGrade(GradeSaveRequest request, Long subjectId);
}
