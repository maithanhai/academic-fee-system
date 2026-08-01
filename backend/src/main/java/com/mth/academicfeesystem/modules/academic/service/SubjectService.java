package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.request.SubjectActiveRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.SubjectRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;

public interface SubjectService {
    List<SubjectResponse> getAllSubjects();
    void addSubject(SubjectRequest request);
    void changeActiveSubject(Long subjectId, SubjectActiveRequest request);
    SubjectResponse updateSubject(Long subjectId, SubjectRequest request);
}
