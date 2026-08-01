package com.mth.academicfeesystem.modules.people.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSearchRequest {
    private String username;
    private String fullName;
    private String department;
    private Boolean active;
}
