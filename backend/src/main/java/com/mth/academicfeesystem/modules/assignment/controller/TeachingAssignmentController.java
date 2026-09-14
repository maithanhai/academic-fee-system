package com.mth.academicfeesystem.modules.assignment.controller;

import java.util.List;

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
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkTeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingClassResponse;
import com.mth.academicfeesystem.modules.assignment.service.TeachingAssignmentService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeachingAssignmentController {
    private final TeachingAssignmentService teachingAssignmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/teaching-assignments")
    public ResponseEntity<ApiResponse<List<TeachingAssignmentResponse>>> getAssignments(
            @RequestParam Long academicYearId,
            @RequestParam List<Integer> gradeLevels) {
        List<TeachingAssignmentResponse> responses = teachingAssignmentService.getExistingAssignments(academicYearId,
                gradeLevels);
        return ResponseEntity.ok(new ApiResponse<>("Lấy dữ liệu phân công giáo viên thành công!", responses));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/teaching-assignments/copy-previous")
    public ResponseEntity<ApiResponse<List<TeachingAssignmentResponse>>> copyAssignmentsPreviousYear(
            @RequestParam Long academicYearId,
            @RequestParam List<Integer> gradeLevels) {
        List<TeachingAssignmentResponse> responses = teachingAssignmentService
                .copyAssignmentsPreviousYear(academicYearId, gradeLevels);
        return ResponseEntity
                .ok(new ApiResponse<>("Lấy dữ liệu phân công giáo viên của năm học cũ thành công!", responses));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/teaching-assignments/preview-auto")
    public ResponseEntity<ApiResponse<List<TeachingAssignmentResponse>>> previewAutoAssign(
            @RequestParam Long academicYearId,
            @RequestParam List<Integer> gradeLevels) {
        List<TeachingAssignmentResponse> response = teachingAssignmentService.previewAutoAssign(academicYearId,
                gradeLevels);
        return ResponseEntity.ok(new ApiResponse<>("Phân công tự động thành công", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/teaching-assignments/bulk")
    public ResponseEntity<ApiResponse<Void>> bulkSaveAssignments(
            @Valid @RequestBody BulkTeachingAssignmentRequest request) {
        teachingAssignmentService.bulkSaveAssignments(request);
        return ResponseEntity.ok(new ApiResponse<>("Phân công giáo viên bộ môn thành công"));
    }

    @GetMapping("/teacher/teaching-assignments/academic-years/{academicYearId}")
    public ResponseEntity<ApiResponse<List<TeachingClassResponse>>> getTeachingClasses(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long academicYearId
    ){
        List<TeachingClassResponse> responses = teachingAssignmentService.getTeachingClasses(principal,academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách lớp dạy thành công",responses));
    }
}
