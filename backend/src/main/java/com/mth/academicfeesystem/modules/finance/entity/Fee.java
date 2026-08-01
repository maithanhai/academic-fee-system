package com.mth.academicfeesystem.modules.finance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;

import jakarta.persistence.Column;
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
@Table(name="fees")
public class Fee extends BaseEntity{
    @Column(nullable = false)
    private String name;
    @Column(precision = 12,scale = 0,nullable = false)
    private BigDecimal feeAmount;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive=true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="academic_year_id",nullable = false)
    private AcademicYear academicYear;
}
