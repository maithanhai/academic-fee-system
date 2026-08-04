package com.mth.academicfeesystem.modules.grade.entity;

import com.mth.academicfeesystem.common.entity.AuditableEntity;
import com.mth.academicfeesystem.common.enums.ExamType;
import com.mth.academicfeesystem.modules.academic.entity.Semester;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.people.entity.Student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="grades",uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"student_id","subject_id","semester_id","exam_type","ordinal_number"},
        name = "uk_grade"
    )
})
public class Grade extends AuditableEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id")
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subject_id")
    private Subject subject;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="semester_id")
    private Semester semester;
    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", length = 20)
    private ExamType examType;
    private int ordinalNumber;
    private double scoreValue;
}
