package com.mth.academicfeesystem.modules.academic.dto.response;

import lombok.Builder;

@Builder
public record SubjectResponse (
    Long id,
    String name,
    Boolean active
){
    
}
