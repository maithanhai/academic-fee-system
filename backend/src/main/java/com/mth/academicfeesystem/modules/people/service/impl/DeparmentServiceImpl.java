package com.mth.academicfeesystem.modules.people.service.impl;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.modules.people.dto.response.DepartmentResponse;
import com.mth.academicfeesystem.modules.people.entity.Department;
import com.mth.academicfeesystem.modules.people.mapper.DepartmentMapper;
import com.mth.academicfeesystem.modules.people.repository.DepartmentRepository;
import com.mth.academicfeesystem.modules.people.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeparmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepo;
    private final DepartmentMapper departmentMapper;
    @Override
    public List<DepartmentResponse> getDepartments(){
        List<Department> departments = departmentRepo.findAll();
        return departmentMapper.toListResponse(departments);
    }
}