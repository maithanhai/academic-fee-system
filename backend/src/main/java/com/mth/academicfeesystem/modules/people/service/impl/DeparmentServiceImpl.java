package com.mth.academicfeesystem.modules.people.service.impl;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.people.dto.request.DepartmentRequest;
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

    @Transactional
    @Override
    public DepartmentResponse addDepartment(DepartmentRequest request){
        Department department = departmentMapper.toEntity(request);
        Department existingDepartment = departmentRepo.findByName(department.getName());
        if (existingDepartment != null&& !existingDepartment.getId().equals(department.getId())) {
            throw new DuplicateResourceException("Tổ bộ môn " + department.getName() + " đã tồn tại trong hệ thống");
        }
        Department departmentSaved = departmentRepo.save(department);
        return departmentMapper.toResponse(departmentSaved);
    }

    @Transactional
    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request){
        Department department = departmentRepo.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Tổ bộ môn không tồn tại"));
        return departmentMapper.toResponse(departmentRepo.save(departmentMapper.toEntity(request, department)));
    }
}