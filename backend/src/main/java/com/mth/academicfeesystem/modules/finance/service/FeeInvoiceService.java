package com.mth.academicfeesystem.modules.finance.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeInvoiceSearchRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceConfirmByAdminRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceConfirmByTeacherRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoByAdminRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoByTeacherRequest;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.InvoiceGenerationResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.StudentInvoiceResponse;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

public interface FeeInvoiceService {
        InvoiceGenerationResponse generateInvoices(Long feeId);

        PageResponse<FeeInvoiceResponse> getAdminInvoices(FeeInvoiceSearchRequest request, Pageable pageable);

        FeeInvoiceDetailResponse getAdminInvoice(Long invoiceId);

        // PaymentResponse initiateOnlinePayment(Long invoiceId, Long studentId);

        FeeInvoiceResponse confirmCashPaymentByTeacher(CustomUserPrincipal principal, Long classId,
                        InvoiceConfirmByTeacherRequest request);

        FeeInvoiceResponse confirmCashPaymentByAdmin(CustomUserPrincipal principal,
                        InvoiceConfirmByAdminRequest request);

        FeeInvoiceResponse undoInvoiceByAdmin(CustomUserPrincipal principal, InvoiceUndoByAdminRequest request);

        List<StudentInvoiceResponse> getInvoicesByTeacher(CustomUserPrincipal principal, Long studentId, Long classId,
                        Long academicYearId);

        FeeInvoiceResponse undoInvoiceByTeacher(CustomUserPrincipal principal, Long classId,
                        InvoiceUndoByTeacherRequest request);

        List<StudentInvoiceResponse> getInvoicesByStudent(CustomUserPrincipal principal, Long academicYearId);

        FeeInvoiceDetailResponse getInvoiceByStudent(CustomUserPrincipal principal, Long invoiceId);

        FeeInvoiceDetailResponse payInvoiceByStudent(CustomUserPrincipal principal, Long invoiceId);
}
