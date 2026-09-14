package com.mth.academicfeesystem.modules.finance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InvoiceUndoByAdminRequest(
    @NotNull(message = "ID hóa đơn không được trống")
    Long invoiceId, 
    @NotBlank(message = "Lý do hủy thu tiền mặt không được để trống")
    String undoReason
) {
}
