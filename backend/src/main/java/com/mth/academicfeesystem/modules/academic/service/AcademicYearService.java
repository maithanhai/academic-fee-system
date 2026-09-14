package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.request.AcademicYearRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.AcademicYearResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

public interface AcademicYearService {
    List<AcademicYearResponse> getAllAcademicYears();

    AcademicYearResponse addAcademicYear();

    AcademicYearResponse updateActiveAcademicYear(Long academicYearId, AcademicYearRequest request);

    List<SchoolClassResponse> getClassesByAcademicYearId(Long academicYearId);

    List<AcademicYearResponse> getAcademicYearsByStudentId(CustomUserPrincipal principal);
}
