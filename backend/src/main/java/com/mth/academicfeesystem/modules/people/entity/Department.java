package com.mth.academicfeesystem.modules.people.entity;

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
@Table(name="departments")
public class Department extends BaseEntity{
    @Column(length = 100,nullable = false)
    private String name;
}
