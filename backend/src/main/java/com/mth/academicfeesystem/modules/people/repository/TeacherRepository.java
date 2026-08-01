package com.mth.academicfeesystem.modules.people.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mth.academicfeesystem.modules.people.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher,Long>, JpaSpecificationExecutor<Teacher>{

    
} 
