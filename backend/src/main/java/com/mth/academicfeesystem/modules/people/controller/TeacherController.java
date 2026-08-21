package com.mth.academicfeesystem.modules.people.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.service.TeacherService;
import com.mth.academicfeesystem.modules.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final UserService userService;
    @PutMapping("/admin/teachers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherDetailResponse>> updateTeacher(
        @PathVariable Long id,
        @RequestBody TeacherAdminUpdateRequest request 
    ){
        TeacherDetailResponse response = teacherService.updateTeacher(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thông tin giáo viên thành công",response));
    }

    @PutMapping("/admin/teachers/{id}/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
        @PathVariable Long id
    ){
        userService.resetPassword(id);
        return ResponseEntity.ok(new ApiResponse<>("Reset password successful"));
    }
}
