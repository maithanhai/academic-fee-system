package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.modules.academic.dto.response.CohortResponse;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.academic.mapper.CohortMapper;
import com.mth.academicfeesystem.modules.academic.repository.CohortRepository;
import com.mth.academicfeesystem.modules.academic.service.CohortService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CohortServiceImpl implements CohortService {
    private final CohortRepository cohortRepo;
    private final CohortMapper cohortMapper;

    @Override
    public List<CohortResponse> getAllCohorts() {
        return cohortMapper.toResponseList(cohortRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
    }

    @Transactional 
    @Override
    public CohortResponse addCohort() {
        int currentYear = LocalDate.now().getYear();
        String cohortName = "K" + currentYear;
        if (cohortRepo.existsByName(cohortName)) {
            throw new DuplicateResourceException("Khóa học " + cohortName + " đã tồn tại trong hệ thống");
        }
        Cohort cohort = Cohort.builder()
                .name(cohortName)
                .admissionYear(currentYear)
                .build();
        Cohort cohortSaved = cohortRepo.save(cohort);
        return cohortMapper.toResponse(cohortSaved);
    }
}
