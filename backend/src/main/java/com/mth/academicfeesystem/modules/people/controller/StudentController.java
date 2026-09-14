package com.mth.academicfeesystem.modules.people.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.people.dto.request.StudentAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.ImportStudentsResult;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.service.StudentService;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final UserService userService;
    @PutMapping("/admin/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentDetailResponse>> updateStudent(
        @PathVariable Long id,
        @RequestBody StudentAdminUpdateRequest request
    ){
        StudentDetailResponse response = studentService.updateStudent(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thông tin học sinh thành công",response));
    }

    @PutMapping("/admin/students/{id}/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
        @PathVariable Long id
    ){
        userService.resetPassword(id);
        return ResponseEntity.ok(new ApiResponse<>("Reset password successful"));
    }

    @PostMapping(value = "/admin/students/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImportStudentsResult>> importStudents(
        @RequestParam("file") MultipartFile file,
        @RequestParam("academicYearId") Long academicYearId,
        @RequestParam("cohortId") Long cohortId
    ) {
        ImportStudentsResult result = studentService.importStudentsExcel(file, academicYearId, cohortId);
        return ResponseEntity.ok(new ApiResponse<>(
                "Import thành công " + result.importedCount() + " học sinh" +
                (result.skippedRows().isEmpty() ? "" : ". Bỏ qua " + result.skippedRows().size() + " dòng không hợp lệ."),
                result));
    }

    @PostMapping("/admin/students")
    public ResponseEntity<ApiResponse<?>> registerStudent(
        @Valid @RequestBody RegisterStudentRequest request
    ){
        studentService.registerStudent(request);
        return ResponseEntity.ok(new ApiResponse<>("Tạo tài khoản cho học sinh thành công"));
    }

    @GetMapping("/admin/students/unenrollments")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsUnenrolled(
        @RequestParam Long academicYearId
    ){
        List<StudentResponse> responses = studentService.getStudentsUnenrolled(academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách học sinh chưa có lớp thành công",responses));
    }
}
