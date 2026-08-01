package com.mth.academicfeesystem.modules.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;

public interface SchoolClassRepository extends JpaRepository<SchoolClass,Long>, JpaSpecificationExecutor<SchoolClass> {

    
}
