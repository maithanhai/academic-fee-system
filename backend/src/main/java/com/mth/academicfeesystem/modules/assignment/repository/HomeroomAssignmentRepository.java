package com.mth.academicfeesystem.modules.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.modules.assignment.entity.HomeroomAssignment;

public interface HomeroomAssignmentRepository extends JpaRepository<HomeroomAssignment,Long>{
    Optional<HomeroomAssignment> findBySchoolClassIdAndStatus(Long classId, AssignmentStatus status);
    Optional<HomeroomAssignment> findByTeacherIdAndStatus(Long teacherId, AssignmentStatus status);
    boolean existsByTeacherIdAndSchoolClassIdAndStatus(Long teacherId, Long classId, AssignmentStatus status);
}
