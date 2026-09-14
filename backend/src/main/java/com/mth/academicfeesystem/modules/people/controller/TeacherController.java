package com.mth.academicfeesystem.modules.people.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherResponse;
import com.mth.academicfeesystem.modules.people.service.TeacherService;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final UserService userService;

    @GetMapping("/admin/teachers/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getActiveTeachers(){
        List<TeacherResponse> responses = teacherService.getActiveTeachers();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách giáo viên với trạng thái hoạt động thành công",responses));
    }

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

    @PostMapping("/admin/teachers")
    public ResponseEntity<ApiResponse<?>> registerTeacher(
        @Valid @RequestBody RegisterTeacherRequest request
    ){
        teacherService.registerTeacher(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo tài khoản cho giáo viên thành công"));
    }
}
