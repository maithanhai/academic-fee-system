package com.mth.academicfeesystem.modules.academic.entity;

import com.mth.academicfeesystem.common.entity.AuditableEntity;

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
@Table(name="subjects")
public class Subject extends AuditableEntity{
    @Column(length = 100,unique = true,nullable = false)
    private String name;
    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive=true;
}
