package com.mth.academicfeesystem.modules.people.dto.response;

public record TeacherResponse (
    String username,
    String fullName,
    DepartmentResponse department,
    Boolean active
){}
