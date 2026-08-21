package com.mth.academicfeesystem.modules.people.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.mth.academicfeesystem.modules.people.dto.response.DepartmentResponse;
import com.mth.academicfeesystem.modules.people.entity.Department;
@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentResponse toResponse(Department department);
    List<DepartmentResponse> toListResponse(List<Department> departments);
} 
