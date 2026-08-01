package com.mth.academicfeesystem.modules.academic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
@Mapper(componentModel = "spring")
public interface CohortMapper {
    @Mapping(target = "cohort",source = "name")
    CohortResponse toResponse(Cohort cohort);
}
