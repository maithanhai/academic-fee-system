package com.mth.academicfeesystem.modules.notification.dto.request;

import lombok.Builder;

@Builder
public record NotificationRequest(
    String title,
    String content,
    Boolean active
) {
} 