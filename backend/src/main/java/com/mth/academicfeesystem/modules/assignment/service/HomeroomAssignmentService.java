package com.mth.academicfeesystem.modules.assignment.service;

import com.mth.academicfeesystem.modules.assignment.dto.request.HomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
public interface HomeroomAssignmentService {
    HomeroomAssignmentResponse assignmentHomeroomTeacher(HomeroomAssignmentRequest request);
    void endHomeroomAssignment(Long homeroomAssignmentId);
    HomeroomAssignmentResponse getMyHomeroomClass(Long userId);
}
