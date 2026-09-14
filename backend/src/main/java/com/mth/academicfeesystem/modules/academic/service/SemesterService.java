package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.response.SemesterResponse;

public interface SemesterService {
    List<SemesterResponse> getSemestersByAcademicYearId(Long academicYearId);
    
} 