package com.mth.academicfeesystem.modules.assignment.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.mth.academicfeesystem.modules.assignment.dto.request.HomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
public interface HomeroomAssignmentService {
    HomeroomAssignmentResponse assignmentHomeroomTeacher(HomeroomAssignmentRequest request);
    void endHomeroomAssignment(Long homeroomAssignmentId);
    HomeroomAssignmentResponse getMyHomeroomClass(UserDetails userDetails);
}
