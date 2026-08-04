package com.mth.academicfeesystem.modules.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.assignment.entity.TeachingAssignment;

public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Long> {
    Optional<TeachingAssignment> findBySchoolClassIdAndSubjectId(Long classId, Long subjectId);

}