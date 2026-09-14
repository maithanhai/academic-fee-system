package com.mth.academicfeesystem.modules.finance.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeInvoiceSearchRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceConfirmByAdminRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceConfirmByTeacherRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoByAdminRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoByTeacherRequest;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.InvoiceGenerationResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.StudentInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.service.FeeInvoiceService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeeInvoiceController {
    private final FeeInvoiceService feeInvoiceService;

    @PostMapping("/admin/fees/{feeId}/invoices/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceGenerationResponse>> generateInvoices(
            @PathVariable Long feeId) {
        return ResponseEntity.ok(new ApiResponse<>("Phát hành hóa đơn thành công",
                feeInvoiceService.generateInvoices(feeId)));
    }

    @GetMapping("/admin/invoices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<FeeInvoiceResponse>>> getAdminInvoices(
            @ModelAttribute FeeInvoiceSearchRequest request,
            @PageableDefault(page = 1, size = 10) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách hóa đơn thành công",
                feeInvoiceService.getAdminInvoices(request, pageable)));
    }

    @GetMapping("/admin/invoices/{invoiceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FeeInvoiceDetailResponse>> getAdminInvoice(
            @PathVariable Long invoiceId) {
        return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết hóa đơn thành công",
                feeInvoiceService.getAdminInvoice(invoiceId)));
    }

    @PostMapping("/admin/invoices/undo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FeeInvoiceResponse>> undoInvoiceByAdmin(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody InvoiceUndoByAdminRequest request) {
        FeeInvoiceResponse response = feeInvoiceService.undoInvoiceByAdmin(principal, request);
        return ResponseEntity.ok(new ApiResponse<>("Hoàn tác hóa đơn thành công", response));
    }

    @PostMapping("/teacher/invoices/undo")
    @PreAuthorize("@assignmentGuard.isHomeroomTeacher(principal.id, #classId)")
    public ResponseEntity<ApiResponse<FeeInvoiceResponse>> undoInvoiceByTeacher(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody InvoiceUndoByTeacherRequest request,
            @RequestParam Long classId) {
        FeeInvoiceResponse response = feeInvoiceService.undoInvoiceByTeacher(principal, classId, request);
        return ResponseEntity.ok(new ApiResponse<>("Hoàn tác hóa đơn thành công", response));
    }

    @PostMapping("/teacher/invoices/confirm-cash")
    @PreAuthorize("@assignmentGuard.isHomeroomTeacher(principal.id, #classId)")
    public ResponseEntity<ApiResponse<FeeInvoiceResponse>> confirmCashByTeacher(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam Long classId,
            @RequestBody InvoiceConfirmByTeacherRequest request) {
        FeeInvoiceResponse response = feeInvoiceService.confirmCashPaymentByTeacher(principal, classId, request);
        return ResponseEntity.ok(new ApiResponse<>("Xác nhận thu tiền mặt thành công", response));
    }

    // @GetMapping("/student/invoices")
    // @PreAuthorize("hasRole('STUDENT')")
    // public ResponseEntity<ApiResponse<PageResponse<StudentInvoiceResponse>>>
    // getStudentInvoices(
    // @AuthenticationPrincipal CustomUserPrincipal principal,
    // @ModelAttribute FeeInvoiceSearchRequest request,
    // @PageableDefault(page = 1, size = 10) Pageable pageable) {
    // return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách hóa đơn cá nhân
    // thành công",
    // feeInvoiceService.getStudentInvoices(principal, request, pageable)));
    // }

    // @PostMapping("/student/invoices/{invoiceId}/pay-online")
    // @PreAuthorize("hasRole('STUDENT')")
    // public ResponseEntity<ApiResponse<PaymentResponse>> initiateOnlinePayment(
    // @PathVariable Long invoiceId,
    // @AuthenticationPrincipal CustomUserPrincipal principal) {
    // return ResponseEntity.ok(new ApiResponse<>("Đã tạo yêu cầu thanh toán
    // online",
    // feeInvoiceService.initiateOnlinePayment(invoiceId, principal.getId())));
    // }

    @PostMapping("/admin/invoices/confirm-cash")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FeeInvoiceResponse>> confirmCashByAdmin(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody InvoiceConfirmByAdminRequest request) {
        FeeInvoiceResponse response = feeInvoiceService.confirmCashPaymentByAdmin(principal, request);
        return ResponseEntity.ok(new ApiResponse<>("Xác nhận thu tiền mặt thành công", response));
    }

    @GetMapping("/teacher/students/{studentId}/invoices")
    @PreAuthorize("@assignmentGuard.isHomeroomTeacher(principal.id, #classId)")
    public ResponseEntity<ApiResponse<List<StudentInvoiceResponse>>> getStudentInvoicesByTeacher(
            @PathVariable Long studentId,
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam Long classId,
            @RequestParam Long academicYearId) {

        List<StudentInvoiceResponse> responses = feeInvoiceService.getInvoicesByTeacher(principal, studentId, classId,
                academicYearId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách hóa đơn thành công cho học sinh", responses));
    }

    @GetMapping("/student/invoices")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<StudentInvoiceResponse>>> getStudentInvoicesByStudent(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam Long academicYearId) {
        List<StudentInvoiceResponse> responses = feeInvoiceService.getInvoicesByStudent(principal, academicYearId);
        return ResponseEntity
                .ok(new ApiResponse<>("Lấy danh sách hóa đơn cho học sinh theo năm học thành công", responses));
    }

    @GetMapping("/student/invoices/{invoiceId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<FeeInvoiceDetailResponse>> getInvoiceDetailByStudent(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        FeeInvoiceDetailResponse response = feeInvoiceService.getInvoiceByStudent(principal, invoiceId);
        return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết hóa đơn thanh toán thành công", response));
    }

    @PostMapping("/student/invoices/{invoiceId}/pay")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<FeeInvoiceDetailResponse>> payInvoiceByStudent(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        FeeInvoiceDetailResponse response = feeInvoiceService.payInvoiceByStudent(principal, invoiceId);
        return ResponseEntity.ok(new ApiResponse<>("Thanh toán hóa đơn thành công", response));
    }
}
