package com.mth.academicfeesystem.modules.people.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mth.academicfeesystem.modules.people.dto.request.DepartmentRequest;
import com.mth.academicfeesystem.modules.people.dto.response.DepartmentResponse;
import com.mth.academicfeesystem.modules.people.entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentResponse toResponse(Department department);

    List<DepartmentResponse> toListResponse(List<Department> departments);

    Department toEntity(DepartmentRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "request.name")
    Department toEntity(DepartmentRequest request,@MappingTarget Department department);
}
