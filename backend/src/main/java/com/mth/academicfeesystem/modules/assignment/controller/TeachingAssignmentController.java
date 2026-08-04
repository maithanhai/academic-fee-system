package com.mth.academicfeesystem.modules.assignment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkTeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.service.TeachingAssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeachingAssignmentController {
    private final TeachingAssignmentService teachingAssignmentService;

    @GetMapping("/admin/teaching-assignments/preview-auto")
    public ResponseEntity<ApiResponse<List<TeachingAssignmentResponse>>> previewAutoAssign(
        @RequestParam Integer gradeLevel
    ){
        List<TeachingAssignmentResponse> response = teachingAssignmentService.previewAutoAssign(gradeLevel);
        return ResponseEntity.ok(new ApiResponse<>("Phân công tự động thành công",response));
    }

    @PostMapping("/admin/teaching-assignments/bulk")
    public ResponseEntity<ApiResponse<Void>> bulkSaveAssignments(
        @Valid @RequestBody BulkTeachingAssignmentRequest request){
        teachingAssignmentService.bulkSaveAssignments(request);
        return ResponseEntity.ok(new ApiResponse<>("Phân công giáo viên bộ môn thành công"));
    }
}
// @PreAuthorize("@assignmentGuard.isSubjectTeacher(principal.id, #request.classId, #request.subjectId)")