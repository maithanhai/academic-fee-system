package com.mth.academicfeesystem.modules.academic.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mth.academicfeesystem.modules.academic.dto.request.SubjectRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;
import com.mth.academicfeesystem.modules.academic.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectResponse toResponse(Subject subject);
    
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "active", source = "active")
    Subject toEntity(SubjectRequest request, @MappingTarget Subject subject);

    List<SubjectResponse> toResponseList(List<Subject> subjects);
}
