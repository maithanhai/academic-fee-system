package com.mth.academicfeesystem.modules.academic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.TransferStudentRequest;
import com.mth.academicfeesystem.modules.academic.service.ClassEnrollmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class ClassEnrollmentController {
    private final ClassEnrollmentService classEnrollmentService;
    @PutMapping("/class-enrollments/tranfer")
    public ResponseEntity<ApiResponse<?>> transferStudent(
        @RequestBody @Valid TransferStudentRequest request
    ) {
        classEnrollmentService.transferStudent(request);
    return ResponseEntity.ok(new ApiResponse<>("Chuyển lớp thành công"));
    }
}
