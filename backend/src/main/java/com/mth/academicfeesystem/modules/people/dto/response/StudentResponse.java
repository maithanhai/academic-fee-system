package com.mth.academicfeesystem.modules.people.dto.response;

public record StudentResponse(
    Long id,
    String username,
    String fullName,
    Boolean active,
    String cohort
) {}
