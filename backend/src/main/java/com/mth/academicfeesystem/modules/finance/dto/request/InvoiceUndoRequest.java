package com.mth.academicfeesystem.modules.finance.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InvoiceUndoRequest(
    @NotBlank(message = "Lý do hủy/hoàn tiền không được để trống")
    String undoReason
) {

}
