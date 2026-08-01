package com.mth.academicfeesystem.modules.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;

public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment,Long>{

}
