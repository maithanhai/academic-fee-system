package com.mth.academicfeesystem.modules.user.entity;

import com.mth.academicfeesystem.common.entity.AuditableEntity;
import com.mth.academicfeesystem.common.enums.Gender;

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
@Table(name = "users")
public class User extends AuditableEntity{
    @Column(nullable = false,length = 50,unique = true)
    private String username;
    @Column(nullable = false,length=100)
    private String password;
    @Column(nullable = false, length = 100)
    private String fullName;
    @Column(length = 15)
    private String phone;
    @Column(length = 100,unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;
    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
