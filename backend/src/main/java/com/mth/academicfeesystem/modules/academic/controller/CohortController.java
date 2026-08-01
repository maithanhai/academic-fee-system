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
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class CohortController {
    private final CohortService cohortService;
    @PostMapping("/admin/cohorts")
    public ResponseEntity<ApiResponse<CohortResponse>> addCohort(){
        CohortResponse response = cohortService.addCohort();
        return ResponseEntity.ok(new ApiResponse<>("Cohort added successfully",response));
    }

    @GetMapping("/admin/cohorts")
    public ResponseEntity<ApiResponse<List<CohortResponse>>> getAllCohorts(){
        List<CohortResponse> response = cohortService.getAllCohorts();
        return ResponseEntity.ok(new ApiResponse<>("Get all cohorts successfully",response));
    }

}
