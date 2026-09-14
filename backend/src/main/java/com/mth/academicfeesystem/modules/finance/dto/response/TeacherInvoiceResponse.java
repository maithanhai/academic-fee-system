package com.mth.academicfeesystem.modules.finance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mth.academicfeesystem.common.enums.InvoiceStatus;
import com.mth.academicfeesystem.common.enums.PaymentMethod;

public record TeacherInvoiceResponse(
    Long id,
    Long studentId,
    String studentName,
    String className,
    Long feeId,
    String feeName,
    BigDecimal amount,
    LocalDate dueDate,
    InvoiceStatus status,
    PaymentMethod paymentMethod
) {
}
