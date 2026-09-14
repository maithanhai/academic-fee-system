package com.mth.academicfeesystem.modules.finance.dto.request;

import jakarta.validation.constraints.NotNull;

public record InvoiceConfirmByTeacherRequest(
        @NotNull(message = "ID hóa đơn không được trống")
        Long invoiceId,
        @NotNull(message = "ID học sinh không được trống")
        Long studentId) {

}
