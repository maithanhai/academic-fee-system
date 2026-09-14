package com.mth.academicfeesystem.modules.finance.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeSearchRequest;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeResponse;
import com.mth.academicfeesystem.modules.finance.service.FeeService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeeController {
    private final FeeService feeService;

    @GetMapping("/admin/fees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<FeeResponse>>> getFeesByAdmin(
        @ModelAttribute FeeSearchRequest request,
        @PageableDefault(page = 1,size = 10) Pageable pageable

    ){
        PageResponse<FeeResponse> response = feeService.getFees(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách học phí thành công",response));
    }

    @PostMapping("/admin/fees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FeeDetailResponse>> createFee(
        @Valid @RequestBody FeeRequest request
    ){
        FeeDetailResponse response = feeService.createFee(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo mới học phí thành công",response));
    }

    @PutMapping("/admin/fees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FeeDetailResponse>> updateFee(
        @PathVariable Long id,
        @Valid @RequestBody FeeRequest request
    ){
        FeeDetailResponse response = feeService.updateFee(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thông tin học phí thành công",response));
    }

    
 }
