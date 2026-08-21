package com.mth.academicfeesystem.modules.academic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.SubjectActiveRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.SubjectRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;
import com.mth.academicfeesystem.modules.academic.service.SubjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class SubjectController {
    private final SubjectService subjectService;
    
    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects(){
        List<SubjectResponse> response = subjectService.getAllSubjects();
        return ResponseEntity.ok(new ApiResponse<>("Get list subjects successful",response));
    }
    
    @PostMapping("/subjects")
    public ResponseEntity<ApiResponse<?>> addSubject(SubjectRequest request){
        subjectService.addSubject(request);
        return ResponseEntity.ok(new ApiResponse<>("Add subject successful"));
    }

    @PatchMapping("/subjects/{id}/active")
    public ResponseEntity<ApiResponse<?>> changeActiveSubject(
        @PathVariable Long subjectId,
        @Valid @RequestBody SubjectActiveRequest request
    ){
        subjectService.changeActiveSubject(subjectId, request);
        return ResponseEntity.ok(new ApiResponse<>("Change active subject successful"));
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
        @PathVariable Long subjectId,
        @Valid @RequestBody SubjectRequest request
    ){
        SubjectResponse response = subjectService.updateSubject(subjectId, request);
        return ResponseEntity.ok(new ApiResponse<>("Update subject successful",response));
    }
}
