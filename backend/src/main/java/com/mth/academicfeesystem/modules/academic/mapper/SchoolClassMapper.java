package com.mth.academicfeesystem.modules.academic.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;

@Mapper(componentModel = "spring")
public interface SchoolClassMapper {
    SchoolClassResponse toResponse(SchoolClass schoolClass);
    default PageResponse<SchoolClassResponse> toPageResponse(Page<SchoolClass> page) {
        if (page == null) {
            return null;
        }
        List<SchoolClassResponse> content = page.getContent()
                .stream()
                .map(this::toResponse) 
                .toList();
        return PageResponse.<SchoolClassResponse>builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(content) 
                .build();
    }
}
