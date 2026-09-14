package com.mth.academicfeesystem.modules.people.dto.response;

import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;
public record StudentResponse(
    Long id,
    String username,
    String fullName,
    Boolean active,
    CohortResponse cohort
) {}
