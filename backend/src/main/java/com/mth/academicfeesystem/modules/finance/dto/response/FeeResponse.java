package com.mth.academicfeesystem.modules.finance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FeeResponse(
    Long id,
    String name,
    BigDecimal feeAmount,
    Long invoiceCount,
    Boolean active,
    String academicYearName,
    LocalDate dueDate
) {
} 