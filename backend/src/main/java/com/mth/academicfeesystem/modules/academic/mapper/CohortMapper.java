package com.mth.academicfeesystem.modules.academic.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import com.mth.academicfeesystem.modules.academic.dto.request.CohortRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
@Mapper(componentModel = "spring")
public interface CohortMapper {
    CohortResponse toResponse(Cohort cohort);
    List<CohortResponse> toResponseList(List<Cohort> cohorts);
    @BeanMapping(ignoreByDefault = true)
    Cohort toEntity(CohortRequest request);
}
