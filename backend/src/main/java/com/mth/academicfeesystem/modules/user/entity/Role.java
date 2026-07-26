package com.mth.academicfeesystem.modules.user.entity;

import com.mth.academicfeesystem.common.entity.BaseEntity;
import com.mth.academicfeesystem.common.enums.RoleName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="roles")
public class Role extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(length = 30,nullable = false,unique = true)
    private RoleName name;
}
