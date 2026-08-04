package com.mth.academicfeesystem.modules.audit.dto.response;

import java.time.LocalDateTime;

public record AuditLogResponse(
    Long id,
    String actorFullName,
    String action,
    String targetTable,
    String payload,
    String ipAddress,
    LocalDateTime createdDate
) {}