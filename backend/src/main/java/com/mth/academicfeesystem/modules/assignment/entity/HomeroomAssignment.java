package com.mth.academicfeesystem.modules.assignment.entity;

import java.time.LocalDate;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.people.entity.Teacher;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="homeroom_assignments")
public class HomeroomAssignment extends BaseEntity{
    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AssignmentStatus status=AssignmentStatus.ACTIVE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="class_id",nullable = false)
    private SchoolClass schoolClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="teacher_id",nullable = false)
    private Teacher teacher;

}
