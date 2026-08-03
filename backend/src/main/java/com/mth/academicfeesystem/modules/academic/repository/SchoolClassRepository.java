package com.mth.academicfeesystem.modules.academic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;

public interface SchoolClassRepository extends JpaRepository<SchoolClass,Long>, JpaSpecificationExecutor<SchoolClass> {
    boolean existsByAcademicYearIdAndGradeLevelIn(Long academicYearId, List<Integer> gradeLevel);
    List<SchoolClass> findByAcademicYearIdAndGradeLevelIn(Long academicYearId,List<Integer> gradeLevel);
    List<SchoolClass> findByAcademicYearId(Long academicYearId);
}
