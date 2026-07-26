package com.mth.academicfeesystem.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity extends BaseEntity{
    @CreatedDate
    @Column(name="created_date",updatable=false,nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(name="updated_date")
    private LocalDateTime updatedDate;
}
