package com.mth.academicfeesystem.modules.people.service;

import java.util.List;

import com.mth.academicfeesystem.modules.people.dto.request.DepartmentRequest;
import com.mth.academicfeesystem.modules.people.dto.response.DepartmentResponse;

public interface DepartmentService {
    List<DepartmentResponse> getDepartments();
    DepartmentResponse addDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
}
