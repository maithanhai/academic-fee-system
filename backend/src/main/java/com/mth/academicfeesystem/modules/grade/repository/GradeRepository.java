package com.mth.academicfeesystem.modules.grade.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.common.enums.ExamType;
import com.mth.academicfeesystem.modules.grade.entity.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
        boolean existsByStudentIdAndSubjectIdAndSemesterIdAndExamTypeAndOrdinalNumber(
                        Long studentId, Long subjectId, Long semesterId, ExamType examType, int ordinalNumber);

        @Query("SELECT g FROM Grade g " +
                        "JOIN g.student s " +
                        "JOIN s.enrollments e " +
                        "WHERE e.schoolClass.id = :classId " +
                        "AND g.subject.id = :subjectId " +
                        "AND g.semester.id = :semesterId " +
                        "ORDER BY s.user.fullName ASC")
        List<Grade> findSubjectTeacherGrades(
                        @Param("classId") Long classId,
                        @Param("subjectId") Long subjectId,
                        @Param("semesterId") Long semesterId);

        @Query("SELECT g FROM Grade g JOIN FETCH g.subject WHERE g.student.id = :studentId AND g.semester.id = :semesterId ORDER BY g.subject.name ASC, g.examType ASC")
        List<Grade> findGradesByStudent(
                        @Param("studentId") Long studentId,
                        @Param("semesterId") Long semesterId);

        @EntityGraph(attributePaths = { "semester", "subject" })
        List<Grade> findByStudentId(Long studentId);

        List<Grade> findBySubjectIdAndSemesterIdAndStudentIdIn(Long subjectId, Long semesterId,
                        List<Long> studentIds);

        Optional<Grade> findByStudentIdAndSubjectIdAndSemesterIdAndExamTypeAndOrdinalNumber(
                        Long studentId, Long subjectId, Long semesterId, ExamType examType, Integer ordinalNumber);

        @Query("""
                            SELECT g FROM Grade g
                            WHERE g.student.id = :studentId
                              AND g.semester.academicYear.id = :academicYearId
                        """)
        List<Grade> findByStudentIdAndAcademicYearId(
                        @Param("studentId") Long studentId,
                        @Param("academicYearId") Long academicYearId);
}
