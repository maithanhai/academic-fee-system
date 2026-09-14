package com.mth.academicfeesystem.modules.academic.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassSearchRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassListResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.service.SchoolClassService;
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentShortListResponse;
import com.mth.academicfeesystem.modules.people.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class SchoolClassController {
    private final SchoolClassService schoolClassService;

    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<List<SchoolClassResponse>>> getAllClasses() {
        List<SchoolClassResponse> response = schoolClassService.getClasses();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách lớp học thành công", response));
    }

    @PostMapping("/classes")
    public ResponseEntity<ApiResponse<SchoolClassResponse>> createClass(
            @Valid @RequestBody SchoolClassRequest request) {
        SchoolClassResponse response = schoolClassService.createClass(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo lớp mới thành công", response));
    }

    // @GetMapping("/classes/{id}/students")
    // public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> getStudentsByClass(
    //         @PathVariable Long id,
    //         StudentSearchRequest request,
    //         Pageable pageable) {
    //     request.setClassId(id);
    //     PageResponse<StudentResponse> response = studentService.searchStudents(request, pageable);
    //     return ResponseEntity.ok(new ApiResponse<>("Get student by class successfull", response));
    // }

    @PostMapping("/classes/auto-promote")
    public ResponseEntity<ApiResponse<?>> autoPromote() {
        schoolClassService.autoPromoteStudents();
        return ResponseEntity.ok(new ApiResponse<>("Successful"));
    }

    @GetMapping("/classes/academic-years/{academicYearId}/total")
    public ResponseEntity<ApiResponse<List<SchoolClassListResponse>>> getListClassesByAcademicYearId(
        @PathVariable Long academicYearId
    ){
        List<SchoolClassListResponse> responses = schoolClassService.getClassesByAcademicYearId(academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách lớp học theo năm học thành công",responses));
    }

    @GetMapping("/classes/{classId}/students")
    public ResponseEntity<ApiResponse<List<StudentShortListResponse>>> getStudentsByClassId(
        @PathVariable Long classId
    ){
        List<StudentShortListResponse> responses = schoolClassService.getStudentsByClassId(classId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách học sinh theo lớp thành công",responses));
    }

}
