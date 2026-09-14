package com.mth.academicfeesystem.modules.notification.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.notification.dto.request.NotificationRequest;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationDetailResponse;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationResponse;
import com.mth.academicfeesystem.modules.notification.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);

    List<NotificationResponse> toListResponse(List<Notification> notifications);

    NotificationDetailResponse toDetailResponse(Notification notification);

    @BeanMapping(ignoreByDefault = true,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "active", source = "active")
    Notification toEntity(NotificationRequest request, @MappingTarget Notification notification);

    Notification toEntity(NotificationRequest request);

    default PageResponse<NotificationResponse> toPageResponse(Page<Notification> page) {
        if (page == null) {
            return null;
        }
        List<NotificationResponse> content = page.getContent()
                .stream()
                .map(this::toResponse)
                .toList();
        return PageResponse.<NotificationResponse>builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(content)
                .build();
    }

}