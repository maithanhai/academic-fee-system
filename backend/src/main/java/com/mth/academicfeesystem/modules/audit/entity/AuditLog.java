package com.mth.academicfeesystem.modules.audit.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.modules.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="audit_logs")
public class AuditLog extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    private String action;
    private String targetTable;
    private String payload;
    private String ipAddress;
    @CreationTimestamp
    private LocalDateTime createdDate;
}
