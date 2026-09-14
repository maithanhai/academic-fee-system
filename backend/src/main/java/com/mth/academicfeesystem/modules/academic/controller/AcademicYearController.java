package com.mth.academicfeesystem.modules.academic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.AcademicYearRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.AcademicYearResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SemesterResponse;
import com.mth.academicfeesystem.modules.academic.service.AcademicYearService;
import com.mth.academicfeesystem.modules.academic.service.SemesterService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AcademicYearController {
    private final AcademicYearService academicYearService;
    private final SemesterService semesterService;

    @GetMapping("/academic-years")
    public ResponseEntity<ApiResponse<List<AcademicYearResponse>>> getAllAcademicYears() {
        List<AcademicYearResponse> response = academicYearService.getAllAcademicYears();
        return ResponseEntity.ok(new ApiResponse<>("Lấy tất cả các năm học thành công", response));
    }

    @PostMapping("/admin/academic-years")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> addAcademicYear() {
        AcademicYearResponse response = academicYearService.addAcademicYear();
        return ResponseEntity.ok(new ApiResponse<>("Thêm mới năm học thành công", response));
    }

    @PutMapping("/admin/academic-years/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> updateActiveAcademicYear(
        @PathVariable Long id,
        @RequestBody AcademicYearRequest request
    ){
        AcademicYearResponse response = academicYearService.updateActiveAcademicYear(id,request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật trạng thái năm học thành công",response));
    }

    @GetMapping("/academic-years/{academicYearId}/classes")
    public ResponseEntity<ApiResponse<List<SchoolClassResponse>>> getClassByAcademicYearId(
        @PathVariable Long academicYearId
    ){
        List<SchoolClassResponse> response = academicYearService.getClassesByAcademicYearId(academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách lớp học theo năm học thành công",response));
    }
    
    @GetMapping("/academic-years/{academicYearId}/semesters")
    public ResponseEntity<ApiResponse<List<SemesterResponse>>> getSemesters(
        @PathVariable Long academicYearId
    ){
        List<SemesterResponse> responses = semesterService.getSemestersByAcademicYearId(academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách học kì trong năm học thành công",responses));
    }

    @PreAuthorize ("hasRole('STUDENT')")
    @GetMapping("/student/academic-years")
    public ResponseEntity<ApiResponse<List<AcademicYearResponse>>> getAcademicYearsByStudent(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<AcademicYearResponse> response = academicYearService.getAcademicYearsByStudentId(principal);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách năm học theo học sinh thành công", response));
    }
}
