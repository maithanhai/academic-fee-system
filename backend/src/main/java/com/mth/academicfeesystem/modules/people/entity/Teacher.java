package com.mth.academicfeesystem.modules.people.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mth.academicfeesystem.modules.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Table(name="teachers")
public class Teacher {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;
    @OneToOne
    @MapsId
    @JoinColumn(name="id")
    @JsonIgnore
    private User user;
}
