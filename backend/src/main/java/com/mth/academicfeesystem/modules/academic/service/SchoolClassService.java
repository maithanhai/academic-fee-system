package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassListResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentShortListResponse;

public interface SchoolClassService {
    List<SchoolClassResponse> getClasses();

    SchoolClassResponse createClass(SchoolClassRequest request);

    void autoPromoteStudents();

    List<SchoolClassListResponse> getClassesByAcademicYearId(Long academicYearId);

    List<StudentShortListResponse> getStudentsByClassId(Long classId);
}
