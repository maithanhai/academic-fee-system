package com.mth.academicfeesystem.modules.notification.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    String title,
    LocalDateTime createdDate,
    Boolean active
) {
}
