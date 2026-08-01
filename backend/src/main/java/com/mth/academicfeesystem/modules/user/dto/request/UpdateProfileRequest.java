package com.mth.academicfeesystem.modules.user.dto.request;

import com.mth.academicfeesystem.modules.people.dto.request.StudentRequest;

public record UpdateProfileRequest(
    String phone,
    String email,
    StudentRequest student
) {}
