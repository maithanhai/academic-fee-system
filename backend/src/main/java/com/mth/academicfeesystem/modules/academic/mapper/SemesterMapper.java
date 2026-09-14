package com.mth.academicfeesystem.modules.academic.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.mth.academicfeesystem.modules.academic.dto.response.SemesterResponse;
import com.mth.academicfeesystem.modules.academic.entity.Semester;

@Mapper(componentModel = "spring")
public interface SemesterMapper {
    SemesterResponse toResponse(Semester semester);

    List<SemesterResponse> toListResponse(List<Semester> semesters);
}
