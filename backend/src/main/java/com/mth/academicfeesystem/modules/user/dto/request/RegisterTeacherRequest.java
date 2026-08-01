package com.mth.academicfeesystem.modules.user.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record RegisterTeacherRequest(
    @NotBlank(message = "Username is required")
    String username,
    @NotBlank(message = "FullName is required")
    String fullName,
    @Past @NotNull
    LocalDate dateOfBirth,
    String phone,
    String email,
    @NotBlank(message = "Gender is required")
    String gender,
    @NotNull Long departmentId
) {} 
