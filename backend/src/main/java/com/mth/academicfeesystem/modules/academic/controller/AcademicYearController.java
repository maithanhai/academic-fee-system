package com.mth.academicfeesystem.modules.academic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.AcademicYearResponse;
import com.mth.academicfeesystem.modules.academic.service.AcademicYearService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AcademicYearController {
    private final AcademicYearService academicYearService;

    @GetMapping("/admin/academic-years")
    public ResponseEntity<ApiResponse<List<AcademicYearResponse>>> getAllAcademicYears() {
        List<AcademicYearResponse> response = academicYearService.getAllAcademicYears();
        return ResponseEntity.ok(new ApiResponse<>("Get all academic years successfully", response));
    }

    @PostMapping("/admin/academic-years")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> addAcademicYear() {
        AcademicYearResponse response = academicYearService.addAcademicYear();
        return ResponseEntity.ok(new ApiResponse<>("Add academic year successfully", response));
    }

    @PostMapping("/admin/academic-years/initialize")
    public ResponseEntity<ApiResponse<Void>> initializeNewAcademicTerm() {
        academicYearService.initializeNewAcademicTerm();
        return ResponseEntity.ok(new ApiResponse<>("Initialize new academic term successfully"));
    }
}
