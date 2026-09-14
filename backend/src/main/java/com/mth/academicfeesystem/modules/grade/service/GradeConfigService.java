package com.mth.academicfeesystem.modules.grade.service;

import com.mth.academicfeesystem.modules.grade.dto.request.GradeConfigsRequest;
import com.mth.academicfeesystem.modules.grade.dto.request.UpdateGradeConfigsRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeConfigsResponse;

public interface GradeConfigService {
    GradeConfigsResponse createGradeConfigs(GradeConfigsRequest request);

    GradeConfigsResponse updateGradeConfigs(UpdateGradeConfigsRequest request);

    GradeConfigsResponse getGradeConfigsBySubjectId(Long subjectId);
}
