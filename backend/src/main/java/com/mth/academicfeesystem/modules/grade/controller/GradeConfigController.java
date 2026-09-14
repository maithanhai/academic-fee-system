package com.mth.academicfeesystem.modules.grade.controller;

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
import com.mth.academicfeesystem.modules.grade.dto.request.GradeConfigsRequest;
import com.mth.academicfeesystem.modules.grade.dto.request.UpdateGradeConfigsRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeConfigsResponse;
import com.mth.academicfeesystem.modules.grade.service.GradeConfigService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GradeConfigController {
    private final GradeConfigService gradeConfigService;

    @PostMapping("/admin/grade-configs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GradeConfigsResponse>> createGradeConfigs(
        @Valid @RequestBody GradeConfigsRequest request
    ){
        GradeConfigsResponse response = gradeConfigService.createGradeConfigs(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo cấu hình các cột điểm thành công", response));
    }

    @PutMapping("/admin/grade-configs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GradeConfigsResponse>> updateGradeConfigs(
        @Valid @RequestBody UpdateGradeConfigsRequest request
    ){
        GradeConfigsResponse response = gradeConfigService.updateGradeConfigs(request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật cấu hình các cột điểm thành công", response));
    }

    @GetMapping("/admin/grade-configs/{subjectId}")
    public ResponseEntity<ApiResponse<GradeConfigsResponse>> getGradeConfigsBySubjectId(
        @PathVariable Long subjectId
    ) {
        GradeConfigsResponse response = gradeConfigService.getGradeConfigsBySubjectId(subjectId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách cấu hình điểm thành công", response));
    }
}
