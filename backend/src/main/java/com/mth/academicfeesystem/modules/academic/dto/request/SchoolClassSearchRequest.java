package com.mth.academicfeesystem.modules.academic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassSearchRequest {
    private String className;
    private Integer gradeLevel;
    private Long academicYearId;
}
