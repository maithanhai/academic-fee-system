package com.mth.academicfeesystem.modules.audit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.audit.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByTargetTable(String targetTable, Pageable pageable);
    Page<AuditLog> findByUserId(Long userId, Pageable pageable);
}
