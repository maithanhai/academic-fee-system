package com.mth.academicfeesystem.modules.academic.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassSearchRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.mapper.SchoolClassMapper;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.service.SchoolClassService;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.mapper.StudentMapper;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {
    private final SchoolClassRepository schoolClassRepo;
    private final SchoolClassMapper schoolClassMapper;
    private final AcademicYearRepository academicYearRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    // Nằm trong SchoolClassService.java

    @Override
    public PageResponse<SchoolClassResponse> searchClasses(SchoolClassSearchRequest request, Pageable pageable) {
        Specification<SchoolClass> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getClassName() != null && !request.getClassName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("className")), "%" + request.getClassName().trim().toLowerCase() + "%"));
            }
            if (request.getGradeLevel() != null) {
                predicates.add(cb.equal(root.get("gradeLevel"), request.getGradeLevel()));
            }
            if (request.getAcademicYearId() != null) {
                predicates.add(cb.equal(root.get("academicYear").get("id"), request.getAcademicYearId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<SchoolClass> classPage = schoolClassRepo.findAll(spec, pageable);
        return schoolClassMapper.toPageResponse(classPage);
    }

    @Transactional
    @Override
    public SchoolClassResponse createClass(SchoolClassRequest request) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(request.className());
        schoolClass.setGradeLevel(request.gradeLevel());
        AcademicYear academicYear = academicYearRepo.findById(request.academicYearId()).orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));
        schoolClass.setAcademicYear(academicYear);
        SchoolClass savedClass = schoolClassRepo.save(schoolClass);
        return schoolClassMapper.toResponse(savedClass);
    }
}
