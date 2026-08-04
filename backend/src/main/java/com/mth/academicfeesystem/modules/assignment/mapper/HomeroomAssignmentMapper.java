package com.mth.academicfeesystem.modules.assignment.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mth.academicfeesystem.modules.assignment.dto.request.HomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.entity.HomeroomAssignment;

@Mapper(componentModel = "spring")
public interface HomeroomAssignmentMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "classId", target = "schoolClass.id")
    @Mapping(source = "teacherId", target = "teacher.id")
    HomeroomAssignment toEntity(HomeroomAssignmentRequest request, @MappingTarget HomeroomAssignment homeroomAssignment);

    @Mapping(source = "schoolClass.id", target = "classId")
    @Mapping(source = "schoolClass.name", target = "className")
    @Mapping(source = "teacher.id",target = "teacherId")
    @Mapping(source = "teacher.user.fullName",target = "teacherName")
    HomeroomAssignmentResponse toResponse(HomeroomAssignment homeroomAssignment);
}
