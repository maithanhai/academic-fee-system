package com.mth.academicfeesystem.modules.audit.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.AuditLogResponse;
import com.mth.academicfeesystem.modules.audit.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<AuditLogResponse>> getAll(Pageable pageable) {
        return new ApiResponse<>("Lấy danh sách Audit Log thành công", auditLogService.getAll(pageable));
    }
}
