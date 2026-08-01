package com.mth.academicfeesystem.modules.academic.dto.response;

public record SchoolClassResponse(
    Long id,
    String name,
    Integer gradeLevel,
    AcademicYearResponse academicYear
) {}
