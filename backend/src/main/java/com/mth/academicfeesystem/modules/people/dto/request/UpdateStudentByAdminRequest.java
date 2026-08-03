package com.mth.academicfeesystem.modules.people.dto.request;

import java.time.LocalDate;

import com.mth.academicfeesystem.common.enums.Gender;

public record UpdateStudentByAdminRequest(
    String fullName,
    LocalDate dateOfBirth,
    String phone,
    String email,
    Gender gender,
    String address,
    String phoneParent
) {

}
