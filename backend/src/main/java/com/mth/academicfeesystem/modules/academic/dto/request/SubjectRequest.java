package com.mth.academicfeesystem.modules.academic.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubjectRequest (
    @NotBlank(message = "Subject name is required")
    String name
){

}
