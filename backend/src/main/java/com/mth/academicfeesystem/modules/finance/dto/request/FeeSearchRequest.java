package com.mth.academicfeesystem.modules.finance.dto.request;

public record FeeSearchRequest(
    String keyword,
    Long academicYearId,
    Boolean active,
    Boolean isExpired
) {

}
