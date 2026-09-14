package com.mth.academicfeesystem.modules.people.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.modules.people.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    long countByCohortId(Long cohortId);

    @EntityGraph(attributePaths = { "user", "cohort" })
    @Query("""
                SELECT s FROM Student s
                WHERE s.user.active = true
                AND s.cohort.admissionYear >= :minAdmissionYear
                AND NOT EXISTS (
                    SELECT 1 FROM ClassEnrollment ce
                    WHERE ce.student = s
                    AND ce.status = :status
                    AND ce.schoolClass.academicYear.id = :academicYearId
                )
            """)
    List<Student> findStudentsWithoutActiveEnrollment(
            @Param("academicYearId") Long academicYearId,
            @Param("status") EnrollmentStatus status,
            @Param("minAdmissionYear") Integer minAdmissionYear);

    @EntityGraph(attributePaths = { "user", "cohort" })
    @Query("SELECT s FROM Student s " +
            "WHERE s.user.active = true " +
            "AND s.user.email IS NOT NULL " +
            "AND s.cohort.admissionYear >= :minYear")
    List<Student> findActiveStudentsForNotification(@Param("minYear") Integer minYear);
}