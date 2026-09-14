package com.mth.academicfeesystem.modules.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.modules.assignment.entity.TeachingAssignment;

public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Long> {
        @EntityGraph(attributePaths = { "teacher", "subject", "schoolClass" })
        List<TeachingAssignment> findBySchoolClassIdIn(List<Long> classIds);

        @Query("SELECT ta.teacher.id, COUNT(ta.id) FROM TeachingAssignment ta WHERE ta.schoolClass.academicYear.id = :academicYearId GROUP BY ta.teacher.id")
        List<Object[]> countTeacherWorkloadByAcademicYear(@Param("academicYearId") Long academicYearId);

        @Query("SELECT ta FROM TeachingAssignment ta " +
                        "WHERE ta.schoolClass.academicYear.id = :academicYearId " +
                        "AND ta.schoolClass.gradeLevel IN :gradeLevels")
        @EntityGraph(attributePaths = { "teacher", "teacher.user", "schoolClass", "subject" })
        List<TeachingAssignment> findByAcademicYearAndGradeLevels(
                        @Param("academicYearId") Long academicYearId,
                        @Param("gradeLevels") List<Integer> gradeLevels);

        @EntityGraph(attributePaths = { "teacher", "subject", "schoolClass", "teacher.user" })
        List<TeachingAssignment> findByTeacherIdAndSchoolClass_AcademicYear_Id(Long teacherId, Long academicYearId);

        boolean existsByTeacherIdAndSchoolClassIdAndSubjectId(Long teacherId, Long classId, Long subjectId);
}