package com.mth.academicfeesystem.modules.grade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.grade.dto.request.GradeSaveRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse.StudentGradeRow.GradeDetailResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse;
import com.mth.academicfeesystem.modules.grade.service.GradeService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GradeController {
    private final GradeService gradeService;

    @GetMapping("/admin/grades/students/{studentId}/transcript")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentTranscriptResponse>> getTranscript(
            @PathVariable Long studentId,
            @RequestParam Long classId) {
        StudentTranscriptResponse response = gradeService.getTranscriptByAdmin(studentId, classId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy bảng điểm thành công", response));
    }

    @GetMapping("/teacher/classes/{classId}/subjects/{subjectId}/grades")
    @PreAuthorize("@assignmentGuard.canGradeSubject(principal.id, #classId, #subjectId)")
    public ResponseEntity<ApiResponse<GradeTableResponse>> getGradeTable(
            @PathVariable("classId") Long classId,
            @PathVariable("subjectId") Long subjectId,
            @RequestParam Long semesterId,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        GradeTableResponse response = gradeService.getGradeTable(classId, subjectId, semesterId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy bảng điểm thành công", response));
    }

    @PostMapping("/teacher/classes/{classId}/subjects/{subjectId}/grades/auto-save")
    @PreAuthorize("@assignmentGuard.canGradeSubject(principal.id, #classId, #subjectId)")
    public ResponseEntity<ApiResponse<GradeDetailResponse>> autoSaveGrade(
            @PathVariable Long classId,
            @PathVariable Long subjectId,
            @Valid @RequestBody GradeSaveRequest request) {
        GradeDetailResponse response = gradeService.autoSaveGrade(request,subjectId);
        return ResponseEntity.ok(new ApiResponse<>("Lưu điểm thành công", response));
    }

    @GetMapping("/student/grades/transcript")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<StudentTranscriptResponse>> getTranscriptForStudent(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam Long academicYearId
        ) {
        StudentTranscriptResponse response = gradeService.getTranscriptByStudent(principal, academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy bảng điểm thành công", response));
    }
}
