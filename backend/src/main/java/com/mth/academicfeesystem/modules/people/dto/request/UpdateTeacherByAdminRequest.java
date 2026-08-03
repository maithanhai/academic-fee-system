package com.mth.academicfeesystem.modules.people.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.mth.academicfeesystem.common.enums.Gender;

public record UpdateTeacherByAdminRequest(
    String fullName,
    LocalDate dateOfBirth,
    String phone,
    String email,
    Gender gender,
    Long departmentId,
    Set<Long> subjectIds
) {
}
