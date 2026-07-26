package com.mth.academicfeesystem.modules.grade.entity;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.common.enums.ExamType;
import com.mth.academicfeesystem.modules.academic.entity.Subject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="grade_configs")
public class GradeConfig extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subject_id",nullable = false)
    private Subject subject;
    @Enumerated(EnumType.STRING)
    @Column(length = 20,nullable = false)
    private ExamType examType;
    @Column(nullable = false)
    private Integer coefficient;
    @Column(nullable = false)
    private Integer maxColumn;
}
