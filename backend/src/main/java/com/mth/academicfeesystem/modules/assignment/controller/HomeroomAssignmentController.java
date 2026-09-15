package com.mth.academicfeesystem.modules.assignment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassListResponse;
import com.mth.academicfeesystem.modules.academic.service.SchoolClassService;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkHomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.service.HomeroomAssignmentService;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse;
import com.mth.academicfeesystem.modules.grade.service.GradeService;
import com.mth.academicfeesystem.modules.people.dto.response.StudentShortListResponse;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeroomAssignmentController {
    private final HomeroomAssignmentService homeroomAssignmentService;
    private final SchoolClassService schoolClassService;
    private final GradeService gradeService;

    @PostMapping("/admin/homeroom-assignments")
    public ResponseEntity<ApiResponse<List<HomeroomAssignmentResponse>>> assignTeacher(
            @Valid @RequestBody BulkHomeroomAssignmentRequest request) {
        homeroomAssignmentService.bulkSaveHomeroomAssignments(request);
        return ResponseEntity.ok(new ApiResponse<>("Phân công giáo viên chủ nhiệm thành công"));
    }

    @PutMapping("/admin/homeroom-assignments/{id}/end")
    public ResponseEntity<ApiResponse<Void>> endAssignment(@PathVariable Long id) {
        homeroomAssignmentService.endHomeroomAssignment(id);
        return ResponseEntity.ok(new ApiResponse<>("Kết thúc giáo viên chủ nhiệm thành công"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/homeroom-assignments")
    public ResponseEntity<ApiResponse<List<HomeroomAssignmentResponse>>> getAssignments(
            @RequestParam Long academicYearId) {
        List<HomeroomAssignmentResponse> responses = homeroomAssignmentService.getExistingAssignments(academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy dữ liệu phân công giáo viên thành công!", responses));
    }

    @GetMapping("/teacher/homeroom-classes")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<SchoolClassListResponse>>> getHomeroomClasses(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        List<SchoolClassListResponse> responses = homeroomAssignmentService.getHomeroomeClass(principal);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách lớp chủ nhiệm thành công",responses));
    }

    @GetMapping("/teacher/homeroom-classes/{classId}/students")
    @PreAuthorize("@assignmentGuard.isHomeroomTeacher(principal.id, #classId)")
    public ResponseEntity<ApiResponse<List<StudentShortListResponse>>> getStudentsByClassId(
        @PathVariable Long classId
    ){
        List<StudentShortListResponse> responses = schoolClassService.getStudentsByClassId(classId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách học sinh theo lớp thành công",responses));
    }

    @GetMapping("/teacher/homeroom-classes/{schoolClassId}/students/{studentId}/transcript")
    @PreAuthorize("@assignmentGuard.isHomeroomTeacher(principal.id, #schoolClassId)")
    public ResponseEntity<ApiResponse<StudentTranscriptResponse>> getStudentTranscript(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long schoolClassId,
        @PathVariable Long studentId
    ){
        StudentTranscriptResponse response = gradeService.getTranscriptByTeacher(studentId,schoolClassId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy bảng điểm học sinh thành công", response));
    }
}
