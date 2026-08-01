package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;
import com.mth.academicfeesystem.modules.academic.dto.response.AcademicYearResponse;

public interface AcademicYearService {
    List<AcademicYearResponse> getAllAcademicYears();
    AcademicYearResponse addAcademicYear();
    void initializeNewAcademicTerm();
}
