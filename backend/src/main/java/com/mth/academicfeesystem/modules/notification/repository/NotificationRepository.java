package com.mth.academicfeesystem.modules.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mth.academicfeesystem.modules.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long>, JpaSpecificationExecutor<Notification>{
    List<Notification> findTop20ByActiveTrueOrderByIdDesc();
}
