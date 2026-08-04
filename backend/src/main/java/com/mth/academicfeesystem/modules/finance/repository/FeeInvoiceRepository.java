package com.mth.academicfeesystem.modules.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.finance.entity.FeeInvoice;

public interface FeeInvoiceRepository extends JpaRepository<FeeInvoice,Long>{

    
} 