package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.request.SubjectRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;

public interface SubjectService {
    List<SubjectResponse> getAllSubjects();
    SubjectResponse addSubject(SubjectRequest request);
    SubjectResponse updateSubject(Long id, SubjectRequest request);
    List<SubjectResponse> getActiveSubjects();
}
