package com.mth.academicfeesystem.modules.user.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.mth.academicfeesystem.common.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record RegisterTeacherRequest(
    @NotBlank(message = "FullName is required")
    String fullName,
    @Past @NotNull
    LocalDate dateOfBirth,
    String phone,   
    String email,
    @NotBlank(message = "Gender is required")
    Gender gender,
    @NotNull Long departmentId,
    @NotEmpty(message = "Must choose at least one subject")
    Set<Long> subjectIds
) {} 
