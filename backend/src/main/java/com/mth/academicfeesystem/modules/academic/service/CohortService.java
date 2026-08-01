package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;

public interface CohortService {
    List<CohortResponse> getAllCohorts();
    CohortResponse addCohort();
}
