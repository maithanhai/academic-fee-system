package com.mth.academicfeesystem.modules.people.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.response.ClassEnrollmentDetailResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;

public record StudentDetailResponse(
    Long id,
    String username,
    String fullName,
    String gender,
    LocalDate dateOfBirth,
    String email,
    String phone,
    LocalDateTime createdDate,
    LocalDateTime updatedDate,
    Boolean active,
    String address,
    String phoneParent,
    CohortResponse cohort,
    List<ClassEnrollmentDetailResponse> enrollments
) {}