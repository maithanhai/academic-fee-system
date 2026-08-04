package com.mth.academicfeesystem.modules.audit.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.modules.audit.dto.response.AuditLogResponse;
import com.mth.academicfeesystem.modules.audit.entity.AuditLog;
import com.mth.academicfeesystem.modules.audit.repository.AuditLogRepository;
import com.mth.academicfeesystem.modules.audit.service.AuditLogService;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService{
    private final AuditLogRepository auditLogRepo;
    private final UserRepository userRepo;

    @Transactional
    @Override
    public void save(Long userId, String action, String targetTable, String payload, String ipAddress) {
        AuditLog log = new AuditLog();
        
        if (userId != null) {
            log.setUser(userRepo.getReferenceById(userId));
        }
        
        log.setAction(action);
        log.setTargetTable(targetTable);
        log.setPayload(payload);
        log.setIpAddress(ipAddress);
        
        auditLogRepo.save(log);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AuditLogResponse> getAll(Pageable pageable) {
        return auditLogRepo.findAll(pageable).map(this::toResponse);
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
            log.getId(),
            log.getUser() != null ? log.getUser().getFullName() : "Hệ thống",
            log.getAction(),
            log.getTargetTable(),
            log.getPayload(),
            log.getIpAddress(),
            log.getCreatedDate()
        );
    }
}
