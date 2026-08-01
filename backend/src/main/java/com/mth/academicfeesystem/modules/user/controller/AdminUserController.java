package com.mth.academicfeesystem.modules.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherResponse;
import com.mth.academicfeesystem.modules.people.service.StudentService;
import com.mth.academicfeesystem.modules.people.service.TeacherService;
import com.mth.academicfeesystem.modules.user.dto.request.ChangeActiveRequest;
import com.mth.academicfeesystem.modules.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserService userService;
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> getStudents(
        StudentSearchRequest request,
        @PageableDefault(page = 1, size = 10) Pageable pageable){
        PageResponse<StudentResponse> response = studentService.searchStudents(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Get list students successful",response)); 
    }

    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse<PageResponse<TeacherResponse>>> getTeachers(
        TeacherSearchRequest request,
        @PageableDefault(page = 1,size = 10) Pageable pageable
    ){
        PageResponse<TeacherResponse> response = teacherService.searchTeachers(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Get list teachers successful",response));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDetailResponse>> getStudentById(
        @PathVariable Long id
    ){
        StudentDetailResponse response = studentService.getStudentById(id);
        return ResponseEntity.ok(new ApiResponse<>("Get student successful",response));
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<ApiResponse<TeacherDetailResponse>> getTeacherById(
        @PathVariable Long id
    ){
        TeacherDetailResponse response = teacherService.getTeacherById(id);
        return ResponseEntity.ok(new ApiResponse<>("Get teacher successful",response));
    }

    @PatchMapping("students/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> changeActive(
        @PathVariable Long id,
        @Valid @RequestBody ChangeActiveRequest request
    ){
        userService.changeActive(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Update active successfull"));
    }
}
