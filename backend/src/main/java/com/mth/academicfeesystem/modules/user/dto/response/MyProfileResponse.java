package com.mth.academicfeesystem.modules.user.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mth.academicfeesystem.common.enums.Gender;
import com.mth.academicfeesystem.common.enums.Role;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;

import lombok.Builder;
@Builder
public record MyProfileResponse(
    String fullName,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dateOfBirth,
    Gender gender,
    String email,
    String phone,
    Role role,
    StudentDetailResponse studentDetailResponse,
    TeacherDetailResponse teacherDetailResponse
) {}