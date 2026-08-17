package com.mth.academicfeesystem.modules.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.user.dto.request.ChangePasswordRequest;
import com.mth.academicfeesystem.modules.user.dto.request.UpdateProfileRequest;
import com.mth.academicfeesystem.modules.user.dto.response.MyProfileResponse;
import com.mth.academicfeesystem.modules.user.service.UserService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        MyProfileResponse response = userService.getMyProfile(principal.getId());
        return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin thành công",response));
    }
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<MyProfileResponse>> updateMyProfile(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody UpdateProfileRequest request){
        MyProfileResponse response=userService.updateMyProfile(principal.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thông tin người dùng thành công",response));
    }
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<?>> changePassword(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @Valid @RequestBody ChangePasswordRequest request){
        userService.changePassword(principal.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>("Thay đổi mật khẩu thành công"));
    }
}
