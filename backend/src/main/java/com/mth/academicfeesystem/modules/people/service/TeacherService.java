package com.mth.academicfeesystem.modules.people.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherListResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherResponse;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;

public interface TeacherService {
    List<TeacherResponse> getActiveTeachers();
    PageResponse<TeacherListResponse> searchTeachers(TeacherSearchRequest request, Pageable pageable);
    TeacherDetailResponse getTeacherById(Long id);
    TeacherDetailResponse updateTeacher(Long id, TeacherAdminUpdateRequest request);
    TeacherDetailResponse registerTeacher(RegisterTeacherRequest request);
}
