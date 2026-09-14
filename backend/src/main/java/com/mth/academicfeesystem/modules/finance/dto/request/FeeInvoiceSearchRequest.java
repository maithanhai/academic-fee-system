package com.mth.academicfeesystem.modules.finance.dto.request;

import com.mth.academicfeesystem.common.enums.InvoiceStatus;

public record FeeInvoiceSearchRequest(
    String keyword,
    Long feeId,
    InvoiceStatus status
) {
}
