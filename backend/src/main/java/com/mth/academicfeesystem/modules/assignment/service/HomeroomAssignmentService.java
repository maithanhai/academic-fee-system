package com.mth.academicfeesystem.modules.assignment.service;

import java.util.List;

import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassListResponse;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkHomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

public interface HomeroomAssignmentService {
    void bulkSaveHomeroomAssignments(BulkHomeroomAssignmentRequest request);

    void endHomeroomAssignment(Long homeroomAssignmentId);

    List<HomeroomAssignmentResponse> getExistingAssignments(Long academicYearId);

    List<SchoolClassListResponse> getHomeroomeClass(CustomUserPrincipal principal);
}
