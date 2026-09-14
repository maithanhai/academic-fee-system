package com.mth.academicfeesystem.modules.grade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mth.academicfeesystem.modules.grade.dto.response.GradeResponse;
import com.mth.academicfeesystem.modules.grade.entity.Grade;

@Mapper(componentModel = "spring")
public interface GradeMapper {
    @Mapping(target = "subject",source = "subject.name")
    @Mapping(target = "studentName",source = "student.user.fullName")
    GradeResponse toResponse(Grade grade);
}
