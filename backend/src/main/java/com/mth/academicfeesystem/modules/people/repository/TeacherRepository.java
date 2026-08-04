package com.mth.academicfeesystem.modules.people.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.modules.people.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    @Query("SELECT t FROM Teacher t JOIN TeacherExpertise te ON t.id = te.teacher.id WHERE te.subject.id = :subjectId")
    List<Teacher> findTeachersBySubjectId(@Param("subjectId") Long subjectId);
}
