package com.mth.academicfeesystem.modules.audit.dto.request;

import java.time.LocalDate;

public record GradeAuditLogSearchRequest(
    String actorName,
    String action,
    LocalDate fromDate,
    LocalDate toDate
) {
}
