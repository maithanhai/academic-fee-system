package com.mth.academicfeesystem.modules.academic.entity;

import java.util.ArrayList;
import java.util.List;

import com.mth.academicfeesystem.common.entity.AuditableEntity;
import com.mth.academicfeesystem.modules.grade.entity.GradeConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
@Table(name = "subjects")
public class Subject extends AuditableEntity {
    @Column(length = 100, unique = true, nullable = false)
    private String name;
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<GradeConfig> gradeConfigs = new ArrayList<>();
}
