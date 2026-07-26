package com.mth.academicfeesystem.modules.assignment.entity;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.people.entity.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name="teaching_assignments",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"teacher_id","class_id","subject_id"},
            name = "uk_teaching_assignment"
        )
    }
)
public class TeachingAssignment extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="teacher_id",nullable = false)
    private Teacher teacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="class_id",nullable = false)
    private SchoolClass schoolClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subject_id",nullable = false)
    private Subject subject;
}
