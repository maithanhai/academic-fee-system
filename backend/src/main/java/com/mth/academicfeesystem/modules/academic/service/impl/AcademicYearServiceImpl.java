package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.modules.academic.dto.response.AcademicYearResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.academic.entity.Semester;
import com.mth.academicfeesystem.modules.academic.mapper.AcademicYearMapper;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.CohortRepository;
import com.mth.academicfeesystem.modules.academic.repository.SemesterRepository;
import com.mth.academicfeesystem.modules.academic.service.AcademicYearService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AcademicYearServiceImpl implements AcademicYearService {
    private final AcademicYearRepository academicYearRepo;
    private final AcademicYearMapper academicYearMapper;
    private final CohortRepository cohortRepo;
    private final SemesterRepository semesterRepo;
    @Override
    public List<AcademicYearResponse> getAllAcademicYears(){
        List<AcademicYear> response =  academicYearRepo.findAll();
        return academicYearMapper.toResponseList(response);
    }

    @Transactional
    @Override
    public AcademicYearResponse addAcademicYear() {
        AcademicYear academicYear = new AcademicYear();
        LocalDate currentDate = LocalDate.now();
        academicYear.setName(currentDate.getYear()+" - "+(currentDate.getYear() + 1));
        if (academicYearRepo.existsByName(academicYear.getName())) {
            throw new DuplicateResourceException("Academic year already exists");
        }
        AcademicYear savedAcademicYear = academicYearRepo.save(academicYear);
        return academicYearMapper.toResponse(savedAcademicYear);
    }

    @Transactional
    @Override
    public void initializeNewAcademicTerm() {
        int currentYear = LocalDate.now().getYear();
        int nextYear = currentYear + 1;
        
        String yearName = currentYear + "-" + nextYear; 
        String cohortName = String.valueOf(currentYear);      

        if (academicYearRepo.existsByName(yearName)) {
            throw new DuplicateResourceException("Academic year " + yearName + " already exists!");
        }
        if (cohortRepo.existsByName(cohortName)) {
            throw new DuplicateResourceException("Cohort " + cohortName + " already exists!");
        }

        AcademicYear newYear = new AcademicYear();
        newYear.setName(yearName);
        newYear = academicYearRepo.save(newYear); 
        List<String> semesterNames = Arrays.asList("Học kỳ 1", "Học kỳ 2");
        for (String sName : semesterNames) {
            Semester semester = new Semester();
            semester.setName(sName);
            semester.setAcademicYear(newYear);
            semesterRepo.save(semester);
        }
        Cohort newCohort = new Cohort();
        newCohort.setName(cohortName);
        cohortRepo.save(newCohort);
    }
}
