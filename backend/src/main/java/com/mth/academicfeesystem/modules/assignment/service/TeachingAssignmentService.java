package com.mth.academicfeesystem.modules.assignment.service;

import java.util.List;

import com.mth.academicfeesystem.modules.assignment.dto.request.BulkTeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingAssignmentResponse;

public interface TeachingAssignmentService {
    List<TeachingAssignmentResponse> previewAutoAssign(Integer gradeLevel);
    void bulkSaveAssignments(BulkTeachingAssignmentRequest request);
}
