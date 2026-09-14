package com.mth.academicfeesystem.modules.finance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;

public record FeeDetailResponse(
    Long id, 
    String name,
    BigDecimal feeAmount,
    LocalDateTime createdDate,
    LocalDate dueDate,
    Boolean active,
    AcademicYear academicYear
) {
}
