package com.mth.academicfeesystem.modules.user.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record RegisterStudentRequest(
    @NotBlank String fullName,
    @NotBlank String gender,
    @NotNull@Past LocalDate dateOfBirth,
    @NotNull Long cohortId,
    @NotNull Long schoolClassId
) {}
