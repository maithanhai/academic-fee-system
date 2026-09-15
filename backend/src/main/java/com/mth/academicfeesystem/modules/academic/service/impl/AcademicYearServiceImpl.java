package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.enums.SemesterName;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.request.AcademicYearRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.AcademicYearResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.entity.Semester;
import com.mth.academicfeesystem.modules.academic.mapper.AcademicYearMapper;
import com.mth.academicfeesystem.modules.academic.mapper.SchoolClassMapper;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.repository.SemesterRepository;
import com.mth.academicfeesystem.modules.academic.service.AcademicYearService;
import com.mth.academicfeesystem.modules.academic.service.ClassEnrollmentService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcademicYearServiceImpl implements AcademicYearService {
    private final AcademicYearRepository academicYearRepo;
    private final AcademicYearMapper academicYearMapper;
    private final SemesterRepository semesterRepo;
    private final SchoolClassRepository schoolClassRepo;
    private final SchoolClassMapper schoolClassMapper;
    private final ClassEnrollmentService classEnrollmentService;
    @Override
    public List<AcademicYearResponse> getAllAcademicYears() {
        List<AcademicYear> response = academicYearRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return academicYearMapper.toResponseList(response);
    }

    @Transactional
    @Override
    public AcademicYearResponse addAcademicYear() {
        AcademicYear academicYear = new AcademicYear();
            LocalDate currentDate = LocalDate.now();
            academicYear.setName(currentDate.getYear() + " - " + (currentDate.getYear() + 1));
        AcademicYear currentAcademicYear = academicYearRepo.findByName(academicYear.getName()).get();
        if (currentAcademicYear.getActive())
            throw new BusinessException("Năm học trước chưa đóng, không thể tạo năm học mới");
        else {
            if (!academicYearRepo.findAll().isEmpty()) {
                if (academicYearRepo.existsByName(academicYear.getName())) {
                    throw new DuplicateResourceException(
                            "Năm học " + academicYear.getName() + " đã tồn tại trong hệ thống");
                }
                AcademicYear savedAcademicYear = academicYearRepo.save(academicYear);
                Semester firstSemester = Semester.builder()
                        .name(SemesterName.FIRST_SEMESTER)
                        .academicYear(savedAcademicYear)
                        .build();
                Semester secondSemester = Semester.builder()
                        .name(SemesterName.SECOND_SEMESTER)
                        .academicYear(savedAcademicYear)
                        .build();
                semesterRepo.saveAll(Arrays.asList(firstSemester, secondSemester));
                return academicYearMapper.toResponse(savedAcademicYear);
            }
        }
        return academicYearMapper.toResponse(academicYear);
    }

    @Override
    public AcademicYearResponse updateActiveAcademicYear(Long academicYearId, AcademicYearRequest request) {
        AcademicYear academicYear = academicYearRepo.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
        if (academicYear.getActive() == request.active())
            return academicYearMapper.toResponse(academicYear);
        academicYear.setActive(request.active());
        academicYearRepo.save(academicYear);
        if (!academicYear.getActive()) {
            classEnrollmentService.dropEnrollmentsByAcademicYearId(academicYearId);
        }
        return academicYearMapper.toResponse(academicYear);
    }

    @Override
    public List<SchoolClassResponse> getClassesByAcademicYearId(Long academicYearId) {
        if (!academicYearRepo.existsById(academicYearId))
            throw new ResourceNotFoundException("Năm học không tồn tại");
        List<SchoolClass> classes = schoolClassRepo.findByAcademicYearId(academicYearId);
        return schoolClassMapper.toListResponse(classes);
    }

    @Override
    public List<AcademicYearResponse> getAcademicYearsByStudentId(CustomUserPrincipal principal) {
        List<AcademicYear> academicYears = academicYearRepo.findAcademicYearsByStudentId(principal.getId());
        return academicYearMapper.toResponseList(academicYears);
    }
}
