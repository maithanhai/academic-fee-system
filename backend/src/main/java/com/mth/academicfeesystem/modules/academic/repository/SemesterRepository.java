package com.mth.academicfeesystem.modules.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.academic.entity.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

}
