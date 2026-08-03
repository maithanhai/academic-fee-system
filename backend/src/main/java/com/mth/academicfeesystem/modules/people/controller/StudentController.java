package com.mth.academicfeesystem.modules.people.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.people.dto.request.UpdateStudentByAdminRequest;
import com.mth.academicfeesystem.modules.people.service.StudentService;
import com.mth.academicfeesystem.modules.user.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final UserService userService;
    @PutMapping("/admin/students/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateStudent(
        @PathVariable Long id,
        @RequestBody UpdateStudentByAdminRequest request
    ){
        studentService.updateStudent(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Update successfull"));
    }

    @PutMapping("/admin/students/{id}/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
        @PathVariable Long id
    ){
        userService.resetPassword(id);
        return ResponseEntity.ok(new ApiResponse<>("Reset password successful"));
    }
}
