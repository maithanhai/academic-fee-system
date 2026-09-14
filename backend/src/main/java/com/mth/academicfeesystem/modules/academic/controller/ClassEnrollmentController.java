package com.mth.academicfeesystem.modules.academic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.EnrollmentRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.TransferRequest;
import com.mth.academicfeesystem.modules.academic.service.ClassEnrollmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class ClassEnrollmentController {
    private final ClassEnrollmentService classEnrollmentService;

    @PostMapping("/admin/class-enrollments/transfer")
    public ResponseEntity<ApiResponse<?>> transferStudent(
            @RequestBody @Valid TransferRequest request) {
        classEnrollmentService.transferStudent(request);
        return ResponseEntity.ok(new ApiResponse<>("Chuyển lớp thành công"));
    }

    @PostMapping("/admin/class-enrollments")
    public ResponseEntity<ApiResponse<?>> createEnrollments(
            @RequestBody @Valid EnrollmentRequest request) {
        classEnrollmentService.enrollStudents(request);
        return ResponseEntity.ok(new ApiResponse<>("Ghi danh thành công"));
    }
}
