package com.mth.academicfeesystem.modules.academic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long>, JpaSpecificationExecutor<SchoolClass> {
    boolean existsByAcademicYearIdAndGradeLevelIn(Long academicYearId, List<Integer> gradeLevel);

    List<SchoolClass> findByAcademicYearIdAndGradeLevelIn(Long academicYearId, List<Integer> gradeLevel);

    List<SchoolClass> findByAcademicYearId(Long academicYearId);

    Optional<SchoolClass> findByAcademicYearIdAndName(Long academicYearId, String name);

    List<SchoolClass> findByGradeLevel(Integer gradeLevel);

    @Query("SELECT ce.schoolClass.id, COUNT(ce.id) " +
            "FROM ClassEnrollment ce " +
            "WHERE ce.schoolClass.id IN :classIds AND ce.status = 'ACTIVE' " +
            "GROUP BY ce.schoolClass.id")
    List<Object[]> countActiveStudentsByClassIds(@Param("classIds") List<Long> classIds);

    @Query("SELECT ce.schoolClass.id, COUNT(ce.id) FROM ClassEnrollment ce " +
            "WHERE ce.schoolClass.id IN :classIds AND ce.status = 'DROPPED' " +
            "GROUP BY ce.schoolClass.id")
    List<Object[]> countHistoricalStudentsByClassIds(@Param("classIds") List<Long> classIds);

    @Query("SELECT ce.schoolClass.id, COUNT(ce.id) " +
            "FROM ClassEnrollment ce " +
            "WHERE ce.schoolClass.id IN :classIds " +
            "AND ce.status = 'ACTIVE' " +
            "GROUP BY ce.schoolClass.id")
    List<Object[]> countStudentsByClassIds(@Param("classIds") List<Long> classIds);
}
