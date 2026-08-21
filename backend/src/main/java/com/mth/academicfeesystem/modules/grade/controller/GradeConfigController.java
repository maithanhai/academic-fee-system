package com.mth.academicfeesystem.modules.grade.controller;

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
import com.mth.academicfeesystem.modules.grade.dto.request.GradeConfigRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.SubjectGradeConfigResponse;
import com.mth.academicfeesystem.modules.grade.service.GradeConfigService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GradeConfigController {
    private final GradeConfigService gradeConfigService;

    @PostMapping("/admin/grade-config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createConfig(
        @Valid @RequestBody GradeConfigRequest request
    ){
        gradeConfigService.createConfig(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo cấu hình các cột điểm thành công"));
    }

    @PutMapping("/admin/grade-config/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateConfig(
        @Valid @RequestBody GradeConfigRequest request,
        @PathVariable Long configId
    ){
        gradeConfigService.updateConfig(configId, request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo cấu hình các cột điểm thành công"));
    }

    @GetMapping("/admin/grade-configs")
    public ResponseEntity<ApiResponse<List<SubjectGradeConfigResponse>>> getAllGradeConfigs() {
        List<SubjectGradeConfigResponse> responses = gradeConfigService.getAllConfigsGroupedBySubject();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách cấu hình điểm thành công", responses));
    }
}
