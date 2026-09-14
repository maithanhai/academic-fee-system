package com.mth.academicfeesystem.modules.academic.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.response.SemesterResponse;
import com.mth.academicfeesystem.modules.academic.entity.Semester;
import com.mth.academicfeesystem.modules.academic.mapper.SemesterMapper;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.SemesterRepository;
import com.mth.academicfeesystem.modules.academic.service.SemesterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
    private final AcademicYearRepository academicYearRepo;
    private final SemesterRepository semesterRepo;
    private final SemesterMapper semesterMapper;

    @Override
    public List<SemesterResponse> getSemestersByAcademicYearId(Long academicYearId) {
        if (!academicYearRepo.existsById(academicYearId))
            throw new ResourceNotFoundException("Năm học không tồn tại");
        List<Semester> semesters = semesterRepo.findAllByAcademicYearId(academicYearId);
        return semesterMapper.toListResponse(semesters);
    }
}
