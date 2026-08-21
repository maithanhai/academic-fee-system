package com.mth.academicfeesystem.modules.people.dto.response;

public record TeacherListResponse (
    Long id,
    String username,
    String fullName,
    DepartmentResponse department,
    Boolean active
){}
