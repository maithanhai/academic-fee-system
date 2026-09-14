package com.mth.academicfeesystem.modules.people.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherExpertiseResponse;
import com.mth.academicfeesystem.modules.people.service.TeacherExpertiseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeacherExpertiseController {
    private final TeacherExpertiseService teacherExpertiseService;

    @GetMapping("/admin/teacher-expertises/workloads")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TeacherExpertiseResponse>>> getTeacherExpertisesWithWorkload(
        @RequestParam Long academicYearId
    ){
        List<TeacherExpertiseResponse> responses = teacherExpertiseService.getTeacherExpertisesWithWorkload(academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách chuyên môn cùng với khối lượng dạy của giáo viên thành công",responses));
    }
}
