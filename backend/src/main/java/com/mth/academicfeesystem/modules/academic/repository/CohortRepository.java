package com.mth.academicfeesystem.modules.academic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.academic.entity.Cohort;

public interface CohortRepository extends JpaRepository<Cohort,Long>{
    boolean existsByName(String name);
    Optional<Cohort> findByName(String name);
} 
