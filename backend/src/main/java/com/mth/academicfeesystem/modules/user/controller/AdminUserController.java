package com.mth.academicfeesystem.modules.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.mth.academicfeesystem.modules.people.dto.response.TeacherListResponse;
import com.mth.academicfeesystem.modules.people.service.StudentService;
import com.mth.academicfeesystem.modules.people.service.TeacherService;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AuthService authService;
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> getStudents(
        @ModelAttribute StudentSearchRequest request,
        @PageableDefault(page = 1, size = 10) Pageable pageable){
        PageResponse<StudentResponse> response = studentService.searchStudents(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách học sinh thành công",response)); 
    }

    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse<PageResponse<TeacherListResponse>>> getTeachers(
        @ModelAttribute TeacherSearchRequest request,
        @PageableDefault(page = 1,size = 10) Pageable pageable
    ){
        PageResponse<TeacherListResponse> response = teacherService.searchTeachers(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách giáo viên thành công",response));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDetailResponse>> getStudentById(
        @PathVariable Long id
    ){
        StudentDetailResponse response = studentService.getStudentById(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin chi tiết học sinh thành công",response));
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<ApiResponse<TeacherDetailResponse>> getTeacherById(
        @PathVariable Long id
    ){
        TeacherDetailResponse response = teacherService.getTeacherById(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách giáo viên chi tiết thành công",response));
    }

    @PostMapping("/students")
    public ResponseEntity<ApiResponse<?>> registerStudent(
        @Valid @RequestBody RegisterStudentRequest request
    ){
        authService.registerStudent(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo tài khoản cho học sinh thành công"));
    }

    @PostMapping("/teachers")
    public ResponseEntity<ApiResponse<?>> registerTeacher(
        @Valid @RequestBody RegisterTeacherRequest request
    ){
        authService.registerTeacher(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo tài khoản cho giáo viên thành công"));
    }

}
