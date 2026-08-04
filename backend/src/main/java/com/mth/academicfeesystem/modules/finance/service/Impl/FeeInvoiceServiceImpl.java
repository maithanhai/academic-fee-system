package com.mth.academicfeesystem.modules.finance.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.InvoiceStatus;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.audit.annotation.Auditable;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoicePaymentRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoRequest;
import com.mth.academicfeesystem.modules.finance.entity.FeeInvoice;
import com.mth.academicfeesystem.modules.finance.repository.FeeInvoiceRepository;
import com.mth.academicfeesystem.modules.finance.service.FeeInvoiceService;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeInvoiceServiceImpl implements FeeInvoiceService {
    private final FeeInvoiceRepository feeInvoiceRepo;
    private final UserRepository userRepo;

    @Transactional
    @Auditable(action = "PAY_INVOICE", targetTable = "fee_invoices")
    @Override
    public void payInvoice(Long invoiceId, Long actionById, InvoicePaymentRequest request) {

        FeeInvoice invoice = feeInvoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BusinessException("Hóa đơn này đã được thanh toán!");
        }

        User actionByUser = userRepo.getReferenceById(actionById);

        // Cập nhật thông tin thanh toán
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaymentMethod(request.paymentMethod());
        invoice.setAmount(request.amount());
        invoice.setActionBy(actionByUser);
        invoice.setUndoReason(null); // Xóa lý do hủy (nếu trước đó đã bị hủy rồi đóng lại)

        feeInvoiceRepo.save(invoice);
    }

    // 2. HỦY / HOÀN TIỀN
    @Transactional
    @Auditable(action = "UNDO_INVOICE", targetTable = "fee_invoices")
    @Override
    public void undoInvoice(Long invoiceId, Long actionById, InvoiceUndoRequest request) {

        FeeInvoice invoice = feeInvoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BusinessException("Hóa đơn này đã bị hủy trước đó!");
        }

        User actionByUser = userRepo.getReferenceById(actionById);

        // Cập nhật trạng thái Hủy
        invoice.setStatus(InvoiceStatus.CANCELLED);
        invoice.setUndoReason(request.undoReason());
        invoice.setActionBy(actionByUser);

        feeInvoiceRepo.save(invoice);
    }
}
