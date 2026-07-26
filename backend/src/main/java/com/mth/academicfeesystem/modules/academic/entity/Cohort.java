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
@Table(name="cohorts")
public class Cohort extends BaseEntity{
    @Column(length = 50,nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private Integer admissionYear;
}
