package com.mth.academicfeesystem.modules.academic.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;

public interface SchoolClassService {
    List<SchoolClassResponse> getClasses();
    SchoolClassResponse createClass(SchoolClassRequest request);
    void autoPromoteStudents();
    int importExcel(MultipartFile file);
}
