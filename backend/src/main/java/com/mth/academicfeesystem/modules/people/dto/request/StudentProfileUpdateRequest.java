package com.mth.academicfeesystem.modules.people.dto.request;

public record StudentProfileUpdateRequest(
    String email,
    String phone,
    String address,
    String phoneParent
) {}
