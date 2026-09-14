package com.mth.academicfeesystem.modules.assignment.service;

import java.util.List;

import com.mth.academicfeesystem.modules.assignment.dto.request.BulkTeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingClassResponse;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

public interface TeachingAssignmentService {
    List<TeachingAssignmentResponse> getExistingAssignments(Long academicYearId, List<Integer> gradeLevels);

    List<TeachingAssignmentResponse> copyAssignmentsPreviousYear(Long academicYearId, List<Integer> gradeLevels);

    List<TeachingAssignmentResponse> previewAutoAssign(Long academicYearId, List<Integer> gradeLevels);

    void bulkSaveAssignments(BulkTeachingAssignmentRequest request);

    List<TeachingClassResponse> getTeachingClasses(CustomUserPrincipal principal, Long academicYearId);
}
