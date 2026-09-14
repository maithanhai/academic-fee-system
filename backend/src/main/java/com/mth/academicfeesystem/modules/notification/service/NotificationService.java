package com.mth.academicfeesystem.modules.notification.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.notification.dto.request.NotificationRequest;
import com.mth.academicfeesystem.modules.notification.dto.request.NotificationSearchRequest;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationDetailResponse;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationResponse;

public interface NotificationService {
    NotificationDetailResponse createNotification(NotificationRequest request);

    NotificationDetailResponse updateNotification(Long notificationId, NotificationRequest request);

    PageResponse<NotificationResponse> searchNotifications(NotificationSearchRequest request, Pageable pageable);

    NotificationDetailResponse getNotificationById(Long notificationId);

    void sendNotificationToStudents(Long notificationId);

    List<NotificationResponse> getNotificationsForStudent();
}