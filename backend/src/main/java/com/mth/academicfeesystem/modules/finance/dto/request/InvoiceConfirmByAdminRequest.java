package com.mth.academicfeesystem.modules.finance.dto.request;

import jakarta.validation.constraints.NotNull;

public record InvoiceConfirmByAdminRequest(
    @NotNull(message = "ID hóa đơn không được trống")
    Long invoiceId
) {

}
