package com.mth.academicfeesystem.modules.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mth.academicfeesystem.modules.finance.entity.Fee;

public interface FeeRepository extends JpaRepository<Fee,Long>,JpaSpecificationExecutor<Fee>{

    List<Fee> findAllByAcademicYearIdAndActiveTrue(Long academicYearId);
} 
