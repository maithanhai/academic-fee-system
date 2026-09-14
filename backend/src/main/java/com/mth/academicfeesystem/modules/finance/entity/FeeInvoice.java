package com.mth.academicfeesystem.modules.finance.entity;

import java.math.BigDecimal;

import com.mth.academicfeesystem.common.entity.AuditableEntity;
import com.mth.academicfeesystem.common.enums.InvoiceStatus;
import com.mth.academicfeesystem.common.enums.PaymentMethod;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="fee_invoices", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "fee_id"}, name = "uk_fee_invoice_student_fee")
})
public class FeeInvoice extends AuditableEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id",nullable = false)
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fee_id",nullable = false)
    private Fee fee;
    @Column(precision = 12,scale = 0,nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(length = 20,nullable = false)
    private InvoiceStatus status;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentMethod paymentMethod;
    @Column(nullable = true)
    private String undoReason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_by")
    private User actionBy;
    @Version
    private Integer version;
}
