package com.mth.academicfeesystem.modules.finance.dto.request;

import java.math.BigDecimal;

import com.mth.academicfeesystem.common.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InvoicePaymentRequest(
    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    BigDecimal amount,

    @NotNull(message = "Phương thức thanh toán không được để trống")
    PaymentMethod paymentMethod
) {

}
