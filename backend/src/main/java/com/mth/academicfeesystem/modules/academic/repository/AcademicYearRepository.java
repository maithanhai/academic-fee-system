package com.mth.academicfeesystem.modules.academic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    boolean existsByName(String name);

    Optional<AcademicYear> findByName(String name);

    @Query("""
                SELECT DISTINCT ay
                FROM ClassEnrollment ce
                JOIN ce.schoolClass sc
                JOIN sc.academicYear ay
                WHERE ce.student.id = :studentId
                ORDER BY ay.id DESC
            """)
    List<AcademicYear> findAcademicYearsByStudentId(@Param("studentId") Long studentId);

}
