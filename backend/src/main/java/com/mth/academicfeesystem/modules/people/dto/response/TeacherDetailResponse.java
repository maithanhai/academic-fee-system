package com.mth.academicfeesystem.modules.people.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;

public record TeacherDetailResponse(
    Long id,
    String username,
    String fullName,
    String gender,
    String email,
    LocalDate dateOfBirth,
    String phone,
    LocalDateTime createdDate,
    LocalDateTime updatedDate,
    Boolean active,
    DepartmentResponse department,
    List<SubjectResponse> subjects
) {}