package com.mth.academicfeesystem.modules.finance.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeRequest;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeResponse;
import com.mth.academicfeesystem.modules.finance.entity.Fee;

@Mapper(componentModel = "spring")
public interface FeeMapper {
    FeeDetailResponse toDetailResponse(Fee fee);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "feeAmount", source = "feeAmount")
    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "active", source = "active")
    Fee toEntity(FeeRequest request);

    @BeanMapping(ignoreByDefault = true,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "feeAmount", source = "feeAmount")
    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "active", source = "active")
    Fee toEntity(FeeRequest request, @MappingTarget Fee fee);

    List<FeeResponse> toResponse(List<Fee> fees);

    @Mapping(target = "academicYearName", source = "academicYear.name")
    FeeResponse toResponse(Fee fee);
    default PageResponse<FeeResponse> toPageResponse(Page<Fee> page) {
        if (page == null) {
            return null;
        }
        
        List<FeeResponse> content = page.getContent()
                .stream()
                .map(this::toResponse) 
                .toList();
                
        return PageResponse.<FeeResponse>builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(content) 
                .build();
    }
}
