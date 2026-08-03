package com.mth.academicfeesystem.modules.academic.service;

import com.mth.academicfeesystem.modules.academic.dto.request.TransferStudentRequest;

public interface ClassEnrollmentService {
    void transferStudent(TransferStudentRequest request);
}
