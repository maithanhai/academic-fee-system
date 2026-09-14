package com.mth.academicfeesystem.modules.academic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;
import com.mth.academicfeesystem.modules.academic.service.CohortService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CohortController {
    private final CohortService cohortService;
    @PostMapping("/admin/cohorts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CohortResponse>> addCohort(){
        CohortResponse response = cohortService.addCohort();
        return ResponseEntity.ok(new ApiResponse<>("Thêm khóa học mới thành công",response));
    }

    @GetMapping("/cohorts")
    public ResponseEntity<ApiResponse<List<CohortResponse>>> getAllCohorts(){
        List<CohortResponse> response = cohortService.getAllCohorts();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách khóa học thành công",response));
    }

}
