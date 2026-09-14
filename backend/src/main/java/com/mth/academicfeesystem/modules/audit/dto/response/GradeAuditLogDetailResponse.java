package com.mth.academicfeesystem.modules.audit.dto.response;

import java.time.LocalDateTime;

public record GradeAuditLogDetailResponse(
    Long id,
    Long actorId,
    String actorFullName,
    String action,
    String payload,
    String ipAddress,
    LocalDateTime createdDate
) {
}
