package com.mth.academicfeesystem.modules.academic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;

public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment,Long>{
    List<ClassEnrollment> findBySchoolClassIdInAndStatus(List<Long> oldCLassIds, EnrollmentStatus status);
    Optional<ClassEnrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
}
