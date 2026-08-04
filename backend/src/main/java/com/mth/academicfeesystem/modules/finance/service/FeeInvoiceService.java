package com.mth.academicfeesystem.modules.finance.service;

import com.mth.academicfeesystem.modules.finance.dto.request.InvoicePaymentRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoRequest;

public interface FeeInvoiceService{
    void payInvoice(Long invoiceId, Long actionById, InvoicePaymentRequest request);
    void undoInvoice(Long invoiceId, Long actionById, InvoiceUndoRequest request);
} 
