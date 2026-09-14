package com.mth.academicfeesystem.modules.people.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.people.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);
}