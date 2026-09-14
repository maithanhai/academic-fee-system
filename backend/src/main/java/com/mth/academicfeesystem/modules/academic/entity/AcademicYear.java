package com.mth.academicfeesystem.modules.academic.entity;

import com.mth.academicfeesystem.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="academic_years")
public class AcademicYear extends BaseEntity{
    @Column(length = 20,nullable = false,unique = true)
    private String name;
    @Builder.Default
    @Column(nullable = false)
    private Boolean active=true;
}
