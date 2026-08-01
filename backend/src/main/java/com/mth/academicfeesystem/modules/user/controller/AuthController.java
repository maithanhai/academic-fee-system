package com.mth.academicfeesystem.modules.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.user.dto.request.LoginRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.dto.response.LoginResponse;
import com.mth.academicfeesystem.modules.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid@RequestBody LoginRequest request){
        LoginResponse response=authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>("Login successful",response));
    }

    @PostMapping("/api/admin/students")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>> registerStudent(
        @Valid @RequestBody RegisterStudentRequest request
    ){
        authService.registerStudent(request);
        return ResponseEntity.ok(new ApiResponse<>("Create account student successfull"));
    }

    @PostMapping("/api/admin/teachers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>> registerTeacher(
        @Valid @RequestBody RegisterTeacherRequest request
    ){
        authService.registerTeacher(request);
        return ResponseEntity.ok(new ApiResponse<>("Create account for teacher successfull"));
    }

    
}
