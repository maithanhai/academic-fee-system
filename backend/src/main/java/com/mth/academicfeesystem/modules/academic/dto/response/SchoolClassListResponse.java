package com.mth.academicfeesystem.modules.academic.dto.response;

import lombok.Builder;

@Builder
public record SchoolClassListResponse(
    Long id, 
    String name,
    Integer gradeLevel,
    Long academicYearId,
    Long totalStudents
) {

}
