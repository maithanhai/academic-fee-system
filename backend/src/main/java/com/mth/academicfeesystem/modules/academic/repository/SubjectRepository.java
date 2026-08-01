package com.mth.academicfeesystem.modules.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.academic.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);
}
