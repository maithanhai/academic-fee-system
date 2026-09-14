package com.mth.academicfeesystem.modules.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import com.mth.academicfeesystem.modules.audit.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {
	Page<AuditLog> findByTargetTableOrderByCreatedDateDesc(String targetTable, Pageable pageable);

	Optional<AuditLog> findByIdAndTargetTable(Long id, String targetTable);
}
