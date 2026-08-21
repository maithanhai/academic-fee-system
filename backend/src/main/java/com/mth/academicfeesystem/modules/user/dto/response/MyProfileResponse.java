package com.mth.academicfeesystem.modules.user.dto.response;

import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;

import lombok.Builder;
@Builder
public record MyProfileResponse(
    StudentDetailResponse studentDetailResponse,
    TeacherDetailResponse teacherDetailResponse
) {}