package com.mth.academicfeesystem.modules.grade.dto.response;

import java.util.List;

public record SubjectGradeConfigResponse(
    Long subjectId,
    String subjectName,
    List<GradeConfigDetailResponse> configs
) {
}
