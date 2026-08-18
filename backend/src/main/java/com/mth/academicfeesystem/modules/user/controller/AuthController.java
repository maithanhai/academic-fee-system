package com.mth.academicfeesystem.modules.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.user.dto.request.LoginRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RefreshTokenRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.dto.response.LoginResponse;
import com.mth.academicfeesystem.modules.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid@RequestBody LoginRequest request){
        LoginResponse response=authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>("Đăng nhập thành công",response));
    }

    @PostMapping("/admin/students")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>> registerStudent(
        @Valid @RequestBody RegisterStudentRequest request
    ){
        authService.registerStudent(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo tài khoản cho học sinh thành công"));
    }

    @PostMapping("/admin/teachers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>> registerTeacher(
        @Valid @RequestBody RegisterTeacherRequest request
    ){
        authService.registerTeacher(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo tài khoản cho giáo viên thành công"));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
        @Valid @RequestBody RefreshTokenRequest request
    ){
        LoginResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(new ApiResponse<>("Lấy token mới thành công",response));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<?>> logout(
        @RequestBody RefreshTokenRequest request
    ){
        authService.logout(request.refreshToken());
        return ResponseEntity.ok(new ApiResponse<>("Đăng xuất thành công"));
    }
    
}
