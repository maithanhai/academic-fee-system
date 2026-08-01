package com.mth.academicfeesystem.modules.academic.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassSearchRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.service.SchoolClassService;
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.service.StudentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ADMIN')")
public class SchoolClassController {
    private final SchoolClassService schoolClassService;
    private final StudentService studentService;
    @GetMapping("/admin/classes")
    public ResponseEntity<ApiResponse<PageResponse<SchoolClassResponse>>> getAllClasses(
        @Valid @RequestBody SchoolClassSearchRequest request,
        Pageable pageable
    ){
        PageResponse<SchoolClassResponse> response=schoolClassService.searchClasses(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Get all classes successfull",response));
    }

    @PostMapping("/admin/classes")
    public ResponseEntity<ApiResponse<SchoolClassResponse>> createClass(
        @Valid @RequestBody SchoolClassRequest request
    ){
        SchoolClassResponse response = schoolClassService.createClass(request);
        return ResponseEntity.ok(new ApiResponse<>("Create class successfull",response));
    }

    @GetMapping("/admin/classes/{id}/students")
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> getStudentsByClass(
            @PathVariable Long id,
            StudentSearchRequest request,
            Pageable pageable
    ) {
        request.setClassId(id); 
        PageResponse<StudentResponse> response = studentService.searchStudents(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Get student by class successfull", response));
    }
}
