package com.mth.academicfeesystem.modules.finance.service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeSearchRequest;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeResponse;
import com.mth.academicfeesystem.modules.finance.entity.Fee;
import com.mth.academicfeesystem.modules.finance.mapper.FeeMapper;
import com.mth.academicfeesystem.modules.finance.repository.FeeRepository;
import com.mth.academicfeesystem.modules.finance.service.FeeService;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {
    private final FeeRepository feeRepo;
    private final FeeMapper feeMapper;
    private final AcademicYearRepository academicYearRepo;

    @Override
    @Transactional
    public FeeDetailResponse createFee(FeeRequest request) {
        AcademicYear academicYear = academicYearRepo.findById(request.academicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại!"));
        Fee fee = feeMapper.toEntity(request);
        fee.setAcademicYear(academicYear);
        feeRepo.save(fee);
        return feeMapper.toDetailResponse(fee);
    }

    @Override
    @Transactional
    public FeeDetailResponse updateFee(Long feeId, FeeRequest request) {
        Fee fee = feeRepo.findById(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Học phí không tồn tại!"));
        AcademicYear academicYear = academicYearRepo.findById(request.academicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại!"));
        Fee newFee = feeMapper.toEntity(request, fee);
        newFee.setAcademicYear(academicYear);
        feeRepo.save(newFee);
        return feeMapper.toDetailResponse(newFee);
    }

    @Override
    public PageResponse<FeeResponse> getFees(FeeSearchRequest request, Pageable pageable) {
        Specification<Fee> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("academicYear", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            var academicYearJoin = root.join("academicYear", JoinType.LEFT);

            if (request.keyword() != null && !request.keyword().trim().isEmpty()) {
                String searchPattern = "%" + request.keyword().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), searchPattern));
            }
            if (request.academicYearId() != null) {
                predicates.add(cb.equal(academicYearJoin.get("id"), request.academicYearId()));
            }
            if (request.active() != null) {
                predicates.add(cb.equal(root.get("active"), request.active()));
            }
            if (request.isExpired() != null) {
                LocalDate today = LocalDate.now();
                if (request.isExpired()) {
                    predicates.add(cb.lessThan(root.get("dueDate"), today));
                } else {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), today));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending());
        Page<Fee> feePage = feeRepo.findAll(spec, sortedPageable);
        return feeMapper.toPageResponse(feePage);
    }
}
