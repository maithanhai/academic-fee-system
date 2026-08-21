package com.mth.academicfeesystem.modules.people.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.people.dto.response.DepartmentResponse;
import com.mth.academicfeesystem.modules.people.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/admin/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartment(){
        List<DepartmentResponse> response = departmentService.getDepartments();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách tổ bộ môn thành công", response));
    }
}
