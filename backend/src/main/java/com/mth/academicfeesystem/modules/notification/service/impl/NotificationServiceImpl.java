package com.mth.academicfeesystem.modules.notification.service.impl;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.notification.dto.request.NotificationRequest;
import com.mth.academicfeesystem.modules.notification.dto.request.NotificationSearchRequest;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationDetailResponse;
import com.mth.academicfeesystem.modules.notification.dto.response.NotificationResponse;
import com.mth.academicfeesystem.modules.notification.entity.Notification;
import com.mth.academicfeesystem.modules.notification.mapper.NotificationMapper;
import com.mth.academicfeesystem.modules.notification.repository.NotificationRepository;
import com.mth.academicfeesystem.modules.notification.service.EmailService;
import com.mth.academicfeesystem.modules.notification.service.NotificationService;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepo;
    private final NotificationMapper notificationMapper;
    private final EmailService emailService;
    private final StudentRepository studentRepo;

    @Override
    public PageResponse<NotificationResponse> searchNotifications(NotificationSearchRequest request,
            Pageable pageable) {
        Specification<Notification> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.title() != null && !request.title().trim().isEmpty()) {
                String searchPattern = "%" + request.title().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("title")), searchPattern));
            }

            if (request.active() != null) {
                predicates.add(cb.equal(root.get("active"), request.active()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending() 
        );
        Page<Notification> notificationPage = notificationRepo.findAll(spec, sortedPageable);
        return notificationMapper.toPageResponse(notificationPage);
    }

    @Transactional
    @Override
    public NotificationDetailResponse createNotification(NotificationRequest request) {
        Notification notification = notificationMapper.toEntity(request);
        notificationRepo.save(notification);
        return notificationMapper.toDetailResponse(notification);
    }

    @Transactional
    @Override
    public NotificationDetailResponse updateNotification(Long notificationId, NotificationRequest request) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Dữ liệu không tồn tại!"));
        Notification newNotification = notificationMapper.toEntity(request, notification);
        notificationRepo.save(newNotification);

        return notificationMapper.toDetailResponse(newNotification);
    }

    @Override
    public NotificationDetailResponse getNotificationById(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Dữ liệu không tồn tại!"));
        return notificationMapper.toDetailResponse(notification);
    }

    @Override
    public void sendNotificationToStudents(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        int currentYear = Year.now().getValue();
        int minYear = currentYear - 2;
        List<Student> students = studentRepo.findActiveStudentsForNotification(minYear);
        if (students.isEmpty()) {
            log.warn("Failed to find any active students for notification.");
            return;
        }
        log.info("Preparing to send notification '{}' to {} students (Years {} to {})...",
                notification.getTitle(), students.size(), minYear, currentYear);
        for (Student student : students) {
            String email = student.getUser().getEmail();
            emailService.sendHtmlEmail(
                    email,
                    notification.getTitle(),
                    notification.getContent());
        }
        log.info("Completed the process of sending bulk notification emails!");
    }

    @Override
    public List<NotificationResponse> getNotificationsForStudent() {
        List<Notification> notifications = notificationRepo.findTop20ByActiveTrueOrderByIdDesc();
        return notificationMapper.toListResponse(notifications);
    }
}
