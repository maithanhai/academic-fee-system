package com.mth.academicfeesystem.modules.finance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FeeRequest(
        @NotBlank(message = "Tên khoản phí không được để trống") String name,
        @NotNull(message = "Số tiền không được để trống") @Positive(message = "Số tiền phải lớn hơn 0") BigDecimal feeAmount,
        @NotNull(message = "Hạn đóng không được để trống") LocalDate dueDate,
        Boolean active,
        @NotNull(message = "Năm học không được để trống") Long academicYearId) {
}