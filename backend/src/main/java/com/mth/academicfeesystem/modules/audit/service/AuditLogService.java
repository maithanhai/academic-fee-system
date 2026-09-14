package com.mth.academicfeesystem.modules.audit.service;

import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.audit.dto.request.GradeAuditLogSearchRequest;
import com.mth.academicfeesystem.modules.audit.dto.request.InvoiceAuditLogSearchRequest;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogResponse;

public interface AuditLogService {
    void save(Long userId, String action, String targetTable, String payload, String ipAddress);

    PageResponse<GradeAuditLogResponse> getGradeLogs(GradeAuditLogSearchRequest request, Pageable pageable);

    GradeAuditLogDetailResponse getGradeLog(Long id);

    PageResponse<InvoiceAuditLogResponse> getInvoiceLogs(InvoiceAuditLogSearchRequest request, Pageable pageable);

    InvoiceAuditLogDetailResponse getInvoiceLog(Long id);
}
