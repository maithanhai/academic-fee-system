package com.mth.academicfeesystem.modules.assignment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.modules.assignment.entity.HomeroomAssignment;

public interface HomeroomAssignmentRepository extends JpaRepository<HomeroomAssignment, Long> {
    @EntityGraph(attributePaths = { "teacher", "teacher.user", "schoolClass" })
    List<HomeroomAssignment> findBySchoolClassIdInAndStatus(List<Long> classIds, AssignmentStatus status);

    @EntityGraph(attributePaths = { "teacher", "teacher.user", "schoolClass" })
    List<HomeroomAssignment> findByTeacherIdInAndStatus(List<Long> teacherIds, AssignmentStatus status);

        @Query("SELECT ha FROM HomeroomAssignment ha "
            + "WHERE ha.teacher.id IN :teacherIds "
            + "AND ha.status = :status "
            + "AND ha.schoolClass.academicYear.id = :academicYearId")
        @EntityGraph(attributePaths = { "teacher", "teacher.user", "schoolClass" })
        List<HomeroomAssignment> findByTeacherIdsAndStatusAndAcademicYearId(
            @Param("teacherIds") List<Long> teacherIds,
            @Param("status") AssignmentStatus status,
            @Param("academicYearId") Long academicYearId);

    Optional<HomeroomAssignment> findByTeacherIdAndStatus(Long teacherId, AssignmentStatus status);

    boolean existsByTeacherIdAndSchoolClassId(Long teacherId, Long classId);

    Optional<HomeroomAssignment> findBySchoolClassIdAndStatus(Long classId, AssignmentStatus status);
    @EntityGraph(attributePaths = {"schoolClass","schoolClass.academicYear"})
    List<HomeroomAssignment> findAllByTeacherId(Long teacherId);
}
