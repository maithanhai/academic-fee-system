package com.mth.academicfeesystem.modules.people.service;

import java.util.List;

import com.mth.academicfeesystem.modules.people.dto.response.TeacherExpertiseResponse;

public interface TeacherExpertiseService {
    List<TeacherExpertiseResponse> getTeacherExpertisesWithWorkload(Long academicYearId);
}
