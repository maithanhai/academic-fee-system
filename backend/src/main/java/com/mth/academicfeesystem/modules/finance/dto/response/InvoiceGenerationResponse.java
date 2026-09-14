package com.mth.academicfeesystem.modules.finance.dto.response;

public record InvoiceGenerationResponse(
    Long feeId,
    int createdCount,
    int skippedCount
) {
}
