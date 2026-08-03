package com.mth.academicfeesystem.modules.academic.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassSearchRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;

public interface SchoolClassService {
    PageResponse<SchoolClassResponse> searchClasses(SchoolClassSearchRequest request, Pageable pageable);
    SchoolClassResponse createClass(SchoolClassRequest request);
    void autoPromoteStudents();
    int importExcel(MultipartFile file);
}
