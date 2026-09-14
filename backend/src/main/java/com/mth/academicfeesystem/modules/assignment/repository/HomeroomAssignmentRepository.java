package com.mth.academicfeesystem.modules.assignment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.modules.assignment.entity.HomeroomAssignment;

public interface HomeroomAssignmentRepository extends JpaRepository<HomeroomAssignment, Long> {
    @EntityGraph(attributePaths = { "teacher", "teacher.user", "schoolClass" })
    List<HomeroomAssignment> findBySchoolClassIdInAndStatus(List<Long> classIds, AssignmentStatus status);

    @EntityGraph(attributePaths = { "teacher", "teacher.user", "schoolClass" })
    List<HomeroomAssignment> findByTeacherIdInAndStatus(List<Long> teacherIds, AssignmentStatus status);

    Optional<HomeroomAssignment> findByTeacherIdAndStatus(Long teacherId, AssignmentStatus status);

    boolean existsByTeacherIdAndSchoolClassIdAndStatus(Long teacherId, Long classId, AssignmentStatus status);

    Optional<HomeroomAssignment> findBySchoolClassIdAndStatus(Long classId, AssignmentStatus status);
    @EntityGraph(attributePaths = {"schoolClass","schoolClass.academicYear"})
    List<HomeroomAssignment> findAllByTeacherId(Long teacherId);
}
