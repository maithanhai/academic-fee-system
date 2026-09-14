package com.mth.academicfeesystem.modules.assignment.dto.response;

import lombok.Builder;

@Builder
public record TeachingClassResponse(
        Long schoolClassId,
        String schoolClassName,
        Long subjectId,
        String subjectName,
        Long teacherId,
        Long academicYearId) {
}
