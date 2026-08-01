package com.mth.academicfeesystem.modules.people.dto.response;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.response.ClassEnrollmentDetailResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;

public record StudentDetailResponse(
    String avatar,
    String address,
    String phoneParent,
    CohortResponse cohort,
    String currentClassName,
    List<ClassEnrollmentDetailResponse> enrollments
) {}