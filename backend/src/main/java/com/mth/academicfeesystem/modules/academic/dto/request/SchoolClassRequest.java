package com.mth.academicfeesystem.modules.academic.dto.request;

public record SchoolClassRequest(
    String className,
    Integer gradeLevel,
    Long academicYearId
) {

}
