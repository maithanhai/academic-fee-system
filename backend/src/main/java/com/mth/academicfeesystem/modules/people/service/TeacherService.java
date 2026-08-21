package com.mth.academicfeesystem.modules.people.service;

import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherListResponse;

public interface TeacherService {
    PageResponse<TeacherListResponse> searchTeachers(TeacherSearchRequest request, Pageable pageable);
    TeacherDetailResponse getTeacherById(Long id);
    TeacherDetailResponse updateTeacher(Long id, TeacherAdminUpdateRequest request);
}
