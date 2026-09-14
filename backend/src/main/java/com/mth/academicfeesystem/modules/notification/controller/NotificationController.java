package com.mth.academicfeesystem.modules.notification.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.notification.dto.request.NotificationRequest;
import com.mth.academicfeesystem.modules.notification.dto.request.NotificationSearchRequest;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationDetailResponse;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationResponse;
import com.mth.academicfeesystem.modules.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {
    private final NotificationService notificationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/notifications")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getNotifications(
            @ModelAttribute NotificationSearchRequest request,
            @PageableDefault(page = 1, size = 10) Pageable pageable) {
        PageResponse<NotificationResponse> response = notificationService.searchNotifications(request, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thông báo thành công", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/notifications")
    public ResponseEntity<ApiResponse<NotificationDetailResponse>> createNotification(
            @RequestBody NotificationRequest request) {
        NotificationDetailResponse response = notificationService.createNotification(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo thông báo mới thành công", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/notifications/{id}")
    public ResponseEntity<ApiResponse<NotificationDetailResponse>> updateNotification(
            @PathVariable Long id,
            @RequestBody NotificationRequest request) {
        NotificationDetailResponse response = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thông báo thành công", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/notifications/{id}")
    public ResponseEntity<ApiResponse<NotificationDetailResponse>> getNotificationById(
            @PathVariable Long id) {
        NotificationDetailResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy dữ liệu thông báo thành công", response));
    }

    @PostMapping("/admin/notifications/{id}/send-to-students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendNotificationToAll(@PathVariable Long id) {
        notificationService.sendNotificationToStudents(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Hệ thống đang gửi email tới học sinh trong trường."));
    }

    @GetMapping("/student/notifications")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsForStudent(){
        List<NotificationResponse> response = notificationService.getNotificationsForStudent();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thông báo thành công", response));
    }

    @GetMapping("/student/notifications/{notificationId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<NotificationDetailResponse>> getNotificationDetail(
        @PathVariable Long notificationId
    ){
        NotificationDetailResponse response = notificationService.getNotificationById(notificationId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy dữ liệu thông báo thành công", response));
    }
}
