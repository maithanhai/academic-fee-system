package com.mth.academicfeesystem.modules.academic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    boolean existsByName(String name);
    Optional<AcademicYear> findByName(String name);
}
