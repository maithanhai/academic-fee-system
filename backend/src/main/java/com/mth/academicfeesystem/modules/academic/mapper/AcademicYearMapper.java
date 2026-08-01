package com.mth.academicfeesystem.modules.academic.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.mth.academicfeesystem.modules.academic.dto.response.AcademicYearResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    List<AcademicYearResponse> toResponseList(List<AcademicYear> academicYears);
    AcademicYearResponse toResponse(AcademicYear academicYear);
}
