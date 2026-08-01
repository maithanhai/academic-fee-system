package com.mth.academicfeesystem.modules.academic.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CohortRequest (
    @NotBlank(message = "Cohort name is required")
    String name
){

}
