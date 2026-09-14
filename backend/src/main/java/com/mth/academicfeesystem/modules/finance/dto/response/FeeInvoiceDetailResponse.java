package com.mth.academicfeesystem.modules.finance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mth.academicfeesystem.common.enums.InvoiceStatus;
import com.mth.academicfeesystem.common.enums.PaymentMethod;

public record FeeInvoiceDetailResponse(
    Long id,
    Long studentId,
    String studentName,
    Long feeId,
    String feeName,
    BigDecimal amount,
    LocalDate dueDate,
    InvoiceStatus status,
    PaymentMethod paymentMethod,
    String undoReason,
    Long actionById,
    String actionByName,
    LocalDateTime createdDate,
    LocalDateTime updatedDate
) {
}
