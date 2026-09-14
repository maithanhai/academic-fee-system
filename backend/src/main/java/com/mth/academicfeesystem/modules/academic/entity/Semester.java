package com.mth.academicfeesystem.modules.academic.entity;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.common.enums.SemesterName;

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
@Table(name="semesters")
public class Semester extends BaseEntity{
    @Column(length = 20,nullable = false)
    @Enumerated(EnumType.STRING)
    private SemesterName name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id",nullable = false)
    private AcademicYear academicYear;
}
