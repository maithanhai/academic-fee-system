package com.mth.academicfeesystem.modules.academic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.SubjectRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;
import com.mth.academicfeesystem.modules.academic.service.SubjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@PreAuthorize("hasRole('ADMIN')")
public class SubjectController {
    private final SubjectService subjectService;
    
    @GetMapping("/admin/subjects")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects(){
        List<SubjectResponse> response = subjectService.getAllSubjects();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách môn học thành công",response));
    }
    
    @PostMapping("/admin/subjects")
    public ResponseEntity<ApiResponse<SubjectResponse>> addSubject(SubjectRequest request){
        SubjectResponse response = subjectService.addSubject(request);
        return ResponseEntity.ok(new ApiResponse<>("Thêm mới môn học thành công", response));
    }

    @PutMapping("/admin/subjects/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
        @PathVariable Long id,
        @Valid @RequestBody SubjectRequest request
    ){
        SubjectResponse response = subjectService.updateSubject(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật môn học thành công",response));
    }

    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getActiveSubjects() {
        List<SubjectResponse> response = subjectService.getActiveSubjects();
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách môn học đang hoạt động thành công", response));
    }
}
