package com.mth.academicfeesystem.modules.people.dto.response;

import lombok.Builder;

@Builder
public record TeacherExpertiseResponse(
    Long subjectId,
    TeacherResponse teacher
) {
    @Builder
    public record TeacherResponse(
        Long teacherId, 
        String teacherName,
        Long workload
    ) {
    }
}
