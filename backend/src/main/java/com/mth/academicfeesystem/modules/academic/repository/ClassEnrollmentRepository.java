package com.mth.academicfeesystem.modules.academic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.people.entity.Student;

public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, Long> {
        List<ClassEnrollment> findBySchoolClassIdInAndStatus(List<Long> oldCLassIds, EnrollmentStatus status);

        Optional<ClassEnrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

        @EntityGraph(attributePaths = { "student", "student.user" })
        List<ClassEnrollment> findBySchoolClassIdAndStatus(Long classId, EnrollmentStatus status);

        Boolean existsBySchoolClassIdAndStudentId(Long schoolClassId, Long studentId);

        @EntityGraph(attributePaths = { "student", "student.user" })
        List<ClassEnrollment> findBySchoolClassId(Long classId);

        @EntityGraph(attributePaths = { "student.user", "schoolClass" })
        @Query("SELECT ce.student FROM ClassEnrollment ce " +
                        "WHERE ce.schoolClass.id = :classId AND ce.status = 'ACTIVE'")
        List<Student> findActiveStudentsByClassId(@Param("classId") Long classId);

        @EntityGraph(attributePaths = { "student.user", "schoolClass" })
        @Query("SELECT ce.student FROM ClassEnrollment ce " +
                        "WHERE ce.schoolClass.id = :classId AND ce.status = 'DROPPED'")
        List<Student> findHistoricalStudentsByClassId(@Param("classId") Long classId);

        @EntityGraph(attributePaths = { "schoolClass.academicYear" })
        @Query("SELECT ce FROM ClassEnrollment ce " +
                        "WHERE ce.schoolClass.academicYear.id = :academicYearId")
        List<ClassEnrollment> findBySchoolClassAcademicYearId(@Param("academicYearId") Long academicYearId);
}
