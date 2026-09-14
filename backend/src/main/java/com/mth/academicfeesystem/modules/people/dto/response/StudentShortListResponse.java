package com.mth.academicfeesystem.modules.people.dto.response;

import java.time.LocalDate;

import com.mth.academicfeesystem.common.enums.Gender;

import lombok.Builder;
@Builder
public record StudentShortListResponse(
    Long enrollmentId,
    Long studentId,
    String studentName, 
    Gender gender,
    LocalDate dayOfBirth
) {

}
