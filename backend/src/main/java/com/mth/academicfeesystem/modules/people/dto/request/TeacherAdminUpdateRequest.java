package com.mth.academicfeesystem.modules.people.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.mth.academicfeesystem.common.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAdminUpdateRequest {
    private String fullName;
    private String phone;
    private Gender gender;
    private Boolean active;
    private LocalDate dateOfBirth;
    private String email;
    private Long departmentId;
    private Set<Long> subjectIds;
}
