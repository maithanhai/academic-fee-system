package com.mth.academicfeesystem.modules.people.service;

import java.util.List;

import com.mth.academicfeesystem.modules.people.dto.response.DepartmentResponse;

public interface DepartmentService {
    List<DepartmentResponse> getDepartments();
}
