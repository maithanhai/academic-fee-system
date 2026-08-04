package com.mth.academicfeesystem.modules.audit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.modules.audit.dto.response.AuditLogResponse;

public interface AuditLogService {
    void save(Long userId, String action, String targetTable, String payload, String ipAddress);
    Page<AuditLogResponse> getAll(Pageable pageable);
} 
