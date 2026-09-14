package com.mth.academicfeesystem.modules.academic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mth.academicfeesystem.modules.academic.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String name);

    @Query("SELECT DISTINCT s FROM Subject s LEFT JOIN FETCH s.gradeConfigs")
    List<Subject> findAllWithGradeConfigs();

    List<Subject> findByActiveTrue();
}
