package com.mth.academicfeesystem.modules.academic.dto.response;

import com.mth.academicfeesystem.common.enums.SemesterName;

import lombok.Builder;
@Builder
public record SemesterResponse(
    Long id, 
    SemesterName name
) {
} 