package com.mth.academicfeesystem.modules.academic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mth.academicfeesystem.modules.academic.dto.response.ClassEnrollmentDetailResponse;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;

@Mapper(componentModel = "spring")
public interface ClassEnrollmentMapper {
    @Mapping(target = "className",source = "schoolClass.name")
    @Mapping(target = "gradeLevel",source = "schoolClass.gradeLevel")
    ClassEnrollmentDetailResponse toDetailResponse(ClassEnrollment classEnrollment);
}