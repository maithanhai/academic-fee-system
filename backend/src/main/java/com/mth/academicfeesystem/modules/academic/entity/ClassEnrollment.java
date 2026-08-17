package com.mth.academicfeesystem.modules.academic.entity;

import java.time.LocalDate;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.modules.people.entity.Student;

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
@Table(name="class_enrollments")
public class ClassEnrollment extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(length = 20,nullable = false)
    private EnrollmentStatus status;
    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="class_id",nullable = false)
    private SchoolClass schoolClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id",nullable = false)
    private Student student;
}
