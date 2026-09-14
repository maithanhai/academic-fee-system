package com.mth.academicfeesystem.modules.audit.dto.request;

import java.time.LocalDate;

public record InvoiceAuditLogSearchRequest(
    String actorName,
    String action,
    LocalDate fromDate,
    LocalDate toDate
) {
}
