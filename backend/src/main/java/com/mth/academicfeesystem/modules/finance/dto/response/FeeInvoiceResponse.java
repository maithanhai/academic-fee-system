package com.mth.academicfeesystem.modules.finance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mth.academicfeesystem.common.enums.InvoiceStatus;
import com.mth.academicfeesystem.common.enums.PaymentMethod;

import lombok.Builder;
@Builder
public record FeeInvoiceResponse(
	Long invoiceId,
	String studentName,
	String feeName,
	BigDecimal amount,
	LocalDate dueDate,
	InvoiceStatus status,
	String undoReason,
	PaymentMethod paymentMethod
) {
}
