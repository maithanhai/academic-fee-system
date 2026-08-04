package com.mth.academicfeesystem.modules.finance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoicePaymentRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoRequest;
import com.mth.academicfeesystem.modules.finance.service.FeeInvoiceService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeeController {
    private final FeeInvoiceService feeInvoiceService;

    @PostMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> payInvoice(
            @PathVariable("id") Long invoiceId,
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody InvoicePaymentRequest request
    ) {
        feeInvoiceService.payInvoice(invoiceId, principal.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>("Thanh toán hóa đơn thành công"));
    }

    @PostMapping("/{id}/undo")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> undoInvoice(
            @PathVariable("id") Long invoiceId,
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody InvoiceUndoRequest request
    ) {
        feeInvoiceService.undoInvoice(invoiceId, principal.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>("Hủy hóa đơn thành công"));
    }
}
