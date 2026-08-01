package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

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
    public List<CohortResponse> getAllCohorts(){
        return cohortMapper.toResponseList(cohortRepo.findAll());
    }

    @Override
    public CohortResponse addCohort() {
        Cohort cohort = new Cohort();
        LocalDate currentDate = LocalDate.now();
        cohort.setName(String.valueOf(currentDate.getYear()));
        if (cohortRepo.existsByName(cohort.getName())){
            throw new DuplicateResourceException("Cohort already exists");
        }
        Cohort cohortSaved = cohortRepo.save(cohort);
        return cohortMapper.toResponse(cohortSaved);
    }
}
