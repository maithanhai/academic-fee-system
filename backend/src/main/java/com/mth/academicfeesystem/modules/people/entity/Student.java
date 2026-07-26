package com.mth.academicfeesystem.modules.people.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.user.entity.User;

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
@Table(name="students")
public class Student {
    @Id
    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneParent;
    private String avatar;
    @OneToOne
    @JoinColumn(name="id")
    @MapsId
    @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name="cohort_id")
    private Cohort cohort;
}
