package com.mth.academicfeesystem.modules.people.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mth.academicfeesystem.modules.people.dto.response.DepartmentResponse;
import com.mth.academicfeesystem.modules.people.entity.Department;
@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    @Mapping(target = "department",source = "name")
    DepartmentResponse toResponse(Department department);
} 
