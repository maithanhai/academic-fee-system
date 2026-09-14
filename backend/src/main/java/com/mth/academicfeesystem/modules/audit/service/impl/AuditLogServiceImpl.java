package com.mth.academicfeesystem.modules.audit.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.audit.dto.request.GradeAuditLogSearchRequest;
import com.mth.academicfeesystem.modules.audit.dto.request.InvoiceAuditLogSearchRequest;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogResponse;
import com.mth.academicfeesystem.modules.audit.entity.AuditLog;
import com.mth.academicfeesystem.modules.audit.mapper.AuditLogMapper;
import com.mth.academicfeesystem.modules.audit.repository.AuditLogRepository;
import com.mth.academicfeesystem.modules.audit.service.AuditLogService;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepo;
    private final UserRepository userRepo;
    private final AuditLogMapper auditLogMapper;

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
    public PageResponse<GradeAuditLogResponse> getGradeLogs(GradeAuditLogSearchRequest request, Pageable pageable) {
        return auditLogMapper.toGradePageResponse(
                search("grades", request.actorName(), request.action(), request.fromDate(), request.toDate(),
                        pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public GradeAuditLogDetailResponse getGradeLog(Long id) {
        return auditLogMapper.toGradeDetailResponse(auditLogRepo.findByIdAndTargetTable(id, "grades")
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy audit log sửa điểm")));
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<InvoiceAuditLogResponse> getInvoiceLogs(InvoiceAuditLogSearchRequest request,
            Pageable pageable) {
        return auditLogMapper.toInvoicePageResponse(
                search("fee_invoices", request.actorName(), request.action(), request.fromDate(), request.toDate(),
                        pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public InvoiceAuditLogDetailResponse getInvoiceLog(Long id) {
        return auditLogMapper.toInvoiceDetailResponse(auditLogRepo.findByIdAndTargetTable(id, "fee_invoices")
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy audit log hóa đơn")));
    }

    private Page<AuditLog> search(String targetTable, String actorName, String action,
            LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        Specification<AuditLog> spec = (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(cb.equal(root.get("targetTable"), targetTable));
            if (actorName != null && !actorName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("user").get("fullName")),
                        "%" + actorName.trim().toLowerCase() + "%"));
            }
            if (action != null && !action.isBlank()) {
                predicates.add(cb.equal(root.get("action"), action));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), fromDate.atStartOfDay()));
            }
            if (toDate != null) {
                predicates.add(cb.lessThan(root.get("createdDate"), toDate.plusDays(1).atStartOfDay()));
            }
            query.orderBy(cb.desc(root.get("createdDate")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return auditLogRepo.findAll(spec, pageable);
    }

}
