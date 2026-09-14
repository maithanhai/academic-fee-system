package com.mth.academicfeesystem.modules.academic.service;

import com.mth.academicfeesystem.modules.academic.dto.request.EnrollmentRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.TransferRequest;

public interface ClassEnrollmentService {
    void enrollStudents(EnrollmentRequest request);
    void transferStudent(TransferRequest request);
    void dropEnrollmentsByAcademicYearId(Long academicYearId);
}
