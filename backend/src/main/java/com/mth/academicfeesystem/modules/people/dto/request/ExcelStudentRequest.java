package com.mth.academicfeesystem.modules.people.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelStudentRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
}
