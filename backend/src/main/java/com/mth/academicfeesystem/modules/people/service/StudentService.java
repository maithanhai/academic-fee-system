package com.mth.academicfeesystem.modules.people.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.StudentAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.ImportStudentsResult;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;

public interface StudentService {
    PageResponse<StudentResponse> searchStudents(StudentSearchRequest request, Pageable pageable);

    StudentDetailResponse getStudentById(Long id);

    StudentDetailResponse updateStudent(Long id, StudentAdminUpdateRequest request);

    ImportStudentsResult importStudentsExcel(MultipartFile file, Long academicYearId, Long cohortId);

    StudentDetailResponse registerStudent(RegisterStudentRequest request);

    List<StudentResponse> getStudentsUnenrolled(Long academicYearId);
}
