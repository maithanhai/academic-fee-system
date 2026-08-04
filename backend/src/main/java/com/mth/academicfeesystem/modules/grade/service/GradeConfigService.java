package com.mth.academicfeesystem.modules.grade.service;

import java.util.List;

import com.mth.academicfeesystem.modules.grade.dto.request.GradeConfigRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.SubjectGradeConfigResponse;

public interface GradeConfigService {
    void createConfig(GradeConfigRequest request);
    void updateConfig(Long configId, GradeConfigRequest request);
    List<SubjectGradeConfigResponse> getAllConfigsGroupedBySubject();
}
