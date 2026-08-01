package com.mth.academicfeesystem.modules.people.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSearchRequest{
    private String username;
    private String fullName;
    private String cohort;
    private Boolean active;
}
