package com.mth.academicfeesystem.modules.user.service;

import com.mth.academicfeesystem.modules.user.dto.request.LoginRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RefreshTokenRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void registerStudent(RegisterStudentRequest request);
    void registerTeacher(RegisterTeacherRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(String refreshToken);
} 
