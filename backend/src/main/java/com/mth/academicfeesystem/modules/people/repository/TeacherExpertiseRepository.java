package com.mth.academicfeesystem.modules.people.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.modules.people.entity.TeacherExpertise;

public interface TeacherExpertiseRepository extends JpaRepository<TeacherExpertise,Long>{
    @Modifying
    @Query("DELETE FROM TeacherExpertise te WHERE te.teacher.id = :teacherId")
    void deleteByTeacherId(@Param("teacherId") Long teacherId);
}
