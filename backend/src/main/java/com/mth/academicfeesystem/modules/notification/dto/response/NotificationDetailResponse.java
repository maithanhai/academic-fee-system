package com.mth.academicfeesystem.modules.notification.dto.response;

public record NotificationDetailResponse(
    Long id,
    String title,
    String content,
    Boolean active
) {
} 