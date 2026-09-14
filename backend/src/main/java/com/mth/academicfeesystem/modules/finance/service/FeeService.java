package com.mth.academicfeesystem.modules.finance.service;

import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeSearchRequest;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeResponse;

public interface FeeService {
    FeeDetailResponse createFee(FeeRequest request);
    FeeDetailResponse updateFee(Long feeId,FeeRequest request);
    PageResponse<FeeResponse> getFees(FeeSearchRequest request,Pageable pageable);
}
