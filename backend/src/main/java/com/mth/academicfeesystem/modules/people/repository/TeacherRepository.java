package com.mth.academicfeesystem.modules.people.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.mth.academicfeesystem.modules.people.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    @EntityGraph(attributePaths = { "user", "expertises" })
    @Query("SELECT t FROM Teacher t WHERE t.user.active=true")
    List<Teacher> findActiveTeacher();

    @EntityGraph(attributePaths = { "expertises", "expertises.subject" })
    @Query("SELECT e.subject.id, t FROM Teacher t LEFT JOIN t.expertises e")
    List<Object[]> findAllTeacherSubjectMappings();

}
