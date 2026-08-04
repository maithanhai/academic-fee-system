package com.mth.academicfeesystem.modules.assignment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.assignment.dto.request.HomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.service.HomeroomAssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeroomAssignmentController {
    private final HomeroomAssignmentService homeroomAssignmentService;

    @PostMapping("/admin/homeroom-assignments")
    public ResponseEntity<ApiResponse<HomeroomAssignmentResponse>> assignTeacher(
        @Valid@RequestBody HomeroomAssignmentRequest request
    ){
        HomeroomAssignmentResponse response = homeroomAssignmentService.assignmentHomeroomTeacher(request);
        return ResponseEntity.ok(new ApiResponse<>("Phân công giáo viên chủ nhiệm thành công",response));
    }

    @PutMapping("/admin/homeroom-assignments/{id}/end")
    public ResponseEntity<ApiResponse<Void>> endAssignment(@PathVariable Long id) {
        homeroomAssignmentService.endHomeroomAssignment(id);
        return ResponseEntity.ok(new ApiResponse<>("Kết thúc giáo viên chủ nhiệm thành công"));
    }

    @GetMapping("/secure/my-homeroom-class")
    public ResponseEntity<ApiResponse<HomeroomAssignmentResponse>> getMyHomeroomClass(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        HomeroomAssignmentResponse response = homeroomAssignmentService.getMyHomeroomClass(userDetails);
        return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin lớp chủ nhiệm thành công", response));
    }
}
