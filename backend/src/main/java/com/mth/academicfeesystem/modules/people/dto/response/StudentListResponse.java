package com.mth.academicfeesystem.modules.people.dto.response;

public record StudentListResponse (
    Long id,
    String username,
    String fullName,
    String cohortName,
    String status
){}
