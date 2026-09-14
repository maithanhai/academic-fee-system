package com.mth.academicfeesystem.modules.audit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.audit.dto.request.GradeAuditLogSearchRequest;
import com.mth.academicfeesystem.modules.audit.dto.request.InvoiceAuditLogSearchRequest;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogResponse;
import com.mth.academicfeesystem.modules.audit.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/grades")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<GradeAuditLogResponse>>> getGradeLogs(
            @ModelAttribute GradeAuditLogSearchRequest request,
            @PageableDefault(page = 1, size = 10) Pageable pageable) {
        PageResponse<GradeAuditLogResponse> pageResponse = auditLogService.getGradeLogs(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách Audit Log sửa điểm thành công", pageResponse));
    }

    @GetMapping("/grades/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GradeAuditLogDetailResponse>> getGradeLog(@PathVariable Long id) {
        GradeAuditLogDetailResponse response = auditLogService.getGradeLog(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết Audit Log sửa điểm thành công", response));
    }

    @GetMapping("/invoices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceAuditLogResponse>>> getInvoiceLogs(
            @ModelAttribute InvoiceAuditLogSearchRequest request,
            @PageableDefault(page = 1, size = 10) Pageable pageable) {
        PageResponse<InvoiceAuditLogResponse> response = auditLogService.getInvoiceLogs(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách Audit Log hóa đơn thành công",
                response));
    }

    @GetMapping("/invoices/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceAuditLogDetailResponse>> getInvoiceLog(@PathVariable Long id) {
        InvoiceAuditLogDetailResponse response = auditLogService.getInvoiceLog(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết Audit Log hóa đơn thành công", response));
    }
}
