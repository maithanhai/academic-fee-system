package com.mth.academicfeesystem.modules.finance.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.enums.InvoiceStatus;
import com.mth.academicfeesystem.common.enums.PaymentMethod;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.ClassEnrollmentRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.assignment.entity.HomeroomAssignment;
import com.mth.academicfeesystem.modules.audit.annotation.Auditable;
import com.mth.academicfeesystem.modules.audit.aspect.AuditContext;
import com.mth.academicfeesystem.modules.finance.dto.request.FeeInvoiceSearchRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceConfirmByAdminRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceConfirmByTeacherRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoByAdminRequest;
import com.mth.academicfeesystem.modules.finance.dto.request.InvoiceUndoByTeacherRequest;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.InvoiceGenerationResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.StudentInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.entity.Fee;
import com.mth.academicfeesystem.modules.finance.entity.FeeInvoice;
import com.mth.academicfeesystem.modules.finance.mapper.FeeInvoiceMapper;
import com.mth.academicfeesystem.modules.finance.repository.FeeInvoiceRepository;
import com.mth.academicfeesystem.modules.finance.repository.FeeRepository;
import com.mth.academicfeesystem.modules.finance.service.FeeInvoiceService;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeInvoiceServiceImpl implements FeeInvoiceService {
    private final FeeInvoiceRepository feeInvoiceRepo;
    private final FeeRepository feeRepo;
    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final AcademicYearRepository academicYearRepo;
    private final ClassEnrollmentRepository classEnrollmentRepo;
    private final SchoolClassRepository schoolClassRepo;
    private final FeeInvoiceMapper feeInvoiceMapper;

    @Transactional
    @Override
    public InvoiceGenerationResponse generateInvoices(Long feeId) {
        Fee fee = feeRepo.findById(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Khoản phí không tồn tại"));
        if (!(fee.getActive())) {
            throw new BusinessException("Không thể phát hành hóa đơn cho khoản phí đã đóng");
        }

        List<Student> students = studentRepo.findAll((root, query, cb) -> {
            var enrollment = root.join("enrollments");
            return cb.and(
                    cb.isTrue(root.get("user").get("active")),
                    cb.equal(enrollment.get("status"), EnrollmentStatus.ACTIVE),
                    cb.equal(enrollment.get("schoolClass").get("academicYear").get("id"),
                            fee.getAcademicYear().getId()));
        });
        Set<Long> existingStudentIds = feeInvoiceRepo.findStudentIdsByFeeId(feeId);
        List<FeeInvoice> invoices = new ArrayList<>();
        int skipped = 0;
        for (Student student : students) {
            if (existingStudentIds.contains(student.getId())) {
                skipped++;
                continue;
            }
            invoices.add(FeeInvoice.builder()
                    .student(student)
                    .fee(fee)
                    .amount(fee.getFeeAmount())
                    .status(InvoiceStatus.UNPAID)
                    .build());
        }
        feeInvoiceRepo.saveAll(invoices);
        return new InvoiceGenerationResponse(feeId, invoices.size(), skipped);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FeeInvoiceResponse> getAdminInvoices(FeeInvoiceSearchRequest request, Pageable pageable) {
        return feeInvoiceMapper.toPageResponse(search(request, null, false, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public FeeInvoiceDetailResponse getAdminInvoice(Long invoiceId) {
        FeeInvoice invoice = feeInvoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        return feeInvoiceMapper.toDetailResponse(invoice);
    }

    private Page<FeeInvoice> search(FeeInvoiceSearchRequest request, Long teacherId, boolean teacherOnly,
            Pageable pageable) {
        Specification<FeeInvoice> spec = baseSpecification(request);
        if (teacherOnly) {
            spec = spec.and((root, query, cb) -> {
                var enrollment = root.join("student").join("enrollments");
                var subquery = query.subquery(Long.class);
                var assignment = subquery.from(HomeroomAssignment.class);
                return cb.and(
                        cb.equal(enrollment.get("status"), EnrollmentStatus.ACTIVE),
                        cb.exists(subquery.select(assignment.get("id"))
                                .where(
                                        cb.equal(assignment.get("teacher").get("id"), teacherId),
                                        cb.equal(assignment.get("schoolClass").get("id"),
                                                enrollment.get("schoolClass").get("id")),
                                        cb.equal(assignment.get("status"), AssignmentStatus.ACTIVE))));
            });
        }
        return feeInvoiceRepo.findAll(spec, pageable);
    }

    private Specification<FeeInvoice> baseSpecification(FeeInvoiceSearchRequest request) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (request != null && request.keyword() != null && !request.keyword().isBlank()) {
                String keyword = "%" + request.keyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("student").get("user").get("fullName")), keyword),
                        cb.like(cb.lower(root.get("fee").get("name")), keyword)));
            }
            if (request != null && request.feeId() != null) {
                predicates.add(cb.equal(root.get("fee").get("id"), request.feeId()));
            }
            if (request != null && request.status() != null) {
                predicates.add(cb.equal(root.get("status"), request.status()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public List<StudentInvoiceResponse> getInvoicesByTeacher(CustomUserPrincipal principal, Long studentId,
            Long classId, Long academicYearId) {
        if (!academicYearRepo.existsById(academicYearId))
            throw new ResourceNotFoundException("Năm học không tồn tại");
        if (!schoolClassRepo.existsById(classId))
            throw new ResourceNotFoundException("Lớp học không tồn tại");
        if (!studentRepo.existsById(studentId))
            throw new ResourceNotFoundException("Học sinh không tồn tại");
        if (!classEnrollmentRepo.existsBySchoolClassIdAndStudentId(classId, studentId))
            throw new BusinessException("Học sinh không thuộc lớp này");
        List<FeeInvoice> feeInvoices = feeInvoiceRepo.findInvoicesByStudentIdAndAcademicYearId(
                studentId,
                academicYearId);
        return feeInvoiceMapper.toListStudentResponses(feeInvoices);
    }

    @Transactional
    @Auditable(action = "PAY_INVOICE", targetTable = "fee_invoices")
    @Override
    public FeeInvoiceResponse confirmCashPaymentByTeacher(CustomUserPrincipal principal, Long classId,
            InvoiceConfirmByTeacherRequest request) {
        if (!classEnrollmentRepo.existsBySchoolClassIdAndStudentId(classId, request.studentId()))
            throw new BusinessException("Học sinh không thuộc lớp học này");
        FeeInvoice invoice = feeInvoiceRepo.findByIdAndStudentId(request.invoiceId(), request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        if (!invoice.getFee().getAcademicYear().getActive()) {
            throw new BusinessException("Năm học của khoản thu này đã đóng");
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BusinessException("Hóa đơn này đã được thanh toán");
        }
        if (invoice.getStatus() == InvoiceStatus.PENDING) {
            throw new BusinessException("Hóa đơn này đang trong quá trình thanh toán");
        }
        AuditContext.setBefore(FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build());
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaymentMethod(PaymentMethod.CASH);
        invoice.setActionBy(userRepo.getReferenceById(principal.getId()));
        invoice.setUndoReason(null);
        feeInvoiceRepo.save(invoice);
        return FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build();
    }

    @Transactional
    @Auditable(action = "PAY_INVOICE", targetTable = "fee_invoices")
    @Override
    public FeeInvoiceResponse confirmCashPaymentByAdmin(CustomUserPrincipal principal,
            InvoiceConfirmByAdminRequest request) {
        FeeInvoice invoice = feeInvoiceRepo.findById(request.invoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BusinessException("Hóa đơn này đã được thanh toán");
        }
        if (invoice.getStatus() == InvoiceStatus.PENDING) {
            throw new BusinessException("Hóa đơn này đang trong quá trình thanh toán");
        }
        AuditContext.setBefore(FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build());
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaymentMethod(PaymentMethod.CASH);
        invoice.setActionBy(userRepo.getReferenceById(principal.getId()));
        invoice.setUndoReason(null);
        feeInvoiceRepo.save(invoice);
        return FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build();
    }

    @Transactional
    @Auditable(action = "UNDO_INVOICE", targetTable = "fee_invoices")
    @Override
    public FeeInvoiceResponse undoInvoiceByAdmin(CustomUserPrincipal principal, InvoiceUndoByAdminRequest request) {
        FeeInvoice invoice = feeInvoiceRepo.findById(request.invoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        if (invoice.getStatus() != InvoiceStatus.PAID) {
            throw new BusinessException("Chỉ được hoàn tác hóa đơn đã thanh toán");
        }
        AuditContext.setBefore(FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build());
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setPaymentMethod(null);
        invoice.setUndoReason(request.undoReason());
        invoice.setActionBy(userRepo.getReferenceById(principal.getId()));
        feeInvoiceRepo.save(invoice);
        return FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .undoReason(invoice.getUndoReason())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build();
    }

    @Transactional
    @Auditable(action = "UNDO_INVOICE", targetTable = "fee_invoices")
    @Override
    public FeeInvoiceResponse undoInvoiceByTeacher(CustomUserPrincipal principal, Long classId,
            InvoiceUndoByTeacherRequest request) {
        if (!classEnrollmentRepo.existsBySchoolClassIdAndStudentId(classId, request.studentId()))
            throw new BusinessException("Học sinh không thuộc lớp học này");
        FeeInvoice invoice = feeInvoiceRepo.findByIdAndStudentId(request.invoiceId(), request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        if (!invoice.getFee().getAcademicYear().getActive()) {
            throw new BusinessException("Năm học của khoản thu này đã đóng");
        }
        if (invoice.getStatus() != InvoiceStatus.PAID) {
            throw new BusinessException("Chỉ được hoàn tác hóa đơn đã thanh toán");
        }
        if (invoice.getPaymentMethod() == PaymentMethod.BANK_TRANSFER) {
            throw new BusinessException("Không hủy được hóa đơn thanh toán trực tuyến");
        }
        AuditContext.setBefore(FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build());
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setPaymentMethod(null);
        invoice.setUndoReason(request.undoReason());
        invoice.setActionBy(userRepo.getReferenceById(principal.getId()));
        feeInvoiceRepo.save(invoice);

        return FeeInvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .dueDate(invoice.getFee().getDueDate())
                .feeName(invoice.getFee().getName())
                .paymentMethod(invoice.getPaymentMethod())
                .status(invoice.getStatus())
                .undoReason(invoice.getUndoReason())
                .studentName(invoice.getStudent().getUser().getFullName())
                .build();
    }

    @Override
    public List<StudentInvoiceResponse> getInvoicesByStudent(CustomUserPrincipal principal, Long academicYearId) {
        if (!academicYearRepo.existsById(academicYearId))
            throw new ResourceNotFoundException("Năm học không tồn tại");
        List<FeeInvoice> feeInvoices = feeInvoiceRepo.findInvoicesByStudentIdAndAcademicYearId(
                principal.getId(),
                academicYearId);
        return feeInvoiceMapper.toListStudentResponses(feeInvoices);
    }

    @Override
    public FeeInvoiceDetailResponse getInvoiceByStudent(CustomUserPrincipal principal, Long invoiceId) {
        FeeInvoice invoice = feeInvoiceRepo.findByIdAndStudentId(invoiceId, principal.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Hóa đơn không tồn tại hoặc không thuộc học sinh này"));
        return feeInvoiceMapper.toDetailResponse(invoice);
    }

    @Transactional
    @Override
    public FeeInvoiceDetailResponse payInvoiceByStudent(CustomUserPrincipal principal, Long invoiceId) {
        FeeInvoice invoice = feeInvoiceRepo.findByIdAndStudentId(invoiceId, principal.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Hóa đơn không tồn tại hoặc không thuộc học sinh này"));
        if (!invoice.getFee().getAcademicYear().getActive()) {
            throw new BusinessException("Năm học của khoản thu này đã đóng");
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BusinessException("Hóa đơn đã được thanh toán");
        }
        if (invoice.getStatus() == InvoiceStatus.PENDING) {
            throw new BusinessException("Hóa đơn đang trong quá trình thanh toán");
        }
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        feeInvoiceRepo.save(invoice);
        return feeInvoiceMapper.toDetailResponse(invoice);
    }
}
