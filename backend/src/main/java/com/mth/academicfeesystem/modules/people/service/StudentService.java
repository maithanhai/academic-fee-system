package com.mth.academicfeesystem.modules.people.service;

import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
public interface StudentService {
    public PageResponse<StudentResponse> searchStudents(StudentSearchRequest request, Pageable pageable);
    public StudentDetailResponse getStudentById(Long id);
}
