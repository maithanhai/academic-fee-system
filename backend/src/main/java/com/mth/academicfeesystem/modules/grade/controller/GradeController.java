package com.mth.academicfeesystem.modules.grade.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.grade.dto.request.GradeRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeDetailResponse;
import com.mth.academicfeesystem.modules.grade.service.GradeService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GradeController {
    private final GradeService gradeService;

    @PostMapping("/teachers/grades")
    @PreAuthorize("@assignmentGuard.isSubjectTeacher(principal.id, #request.classId, #request.subjectId)")
    public ResponseEntity<ApiResponse<Void>> inputGrades(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody GradeRequest request) {
        gradeService.inputGrades(request);
        return ResponseEntity.ok(new ApiResponse<>("Nhập điểm thành công"));
    }

    @GetMapping("/teachers/classes/{classId}/subjects/{subjectId}/grades")
    @PreAuthorize("@assignmentGuard.isSubjectTeacher(principal.id, #classId, #subjectId)")
    public ResponseEntity<ApiResponse<List<GradeDetailResponse>>> getTeacherGradeBoard(
            @PathVariable("classId") Long classId,
            @PathVariable("subjectId") Long subjectId,
            @RequestParam("semesterId") Long semesterId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<GradeDetailResponse> response = gradeService.getSubjectGradeBoard(classId, subjectId, semesterId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy bảng điểm thành công", response));
    }

    @GetMapping("/students/me/grades")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<GradeDetailResponse>>> getMyGrades(
            @RequestParam("semesterId") Long semesterId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<GradeDetailResponse> response = gradeService.getMyGrades(principal.getId(), semesterId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy điểm cá nhân thành công", response));
    }
}
