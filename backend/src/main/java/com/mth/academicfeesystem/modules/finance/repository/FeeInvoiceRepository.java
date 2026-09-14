package com.mth.academicfeesystem.modules.finance.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mth.academicfeesystem.modules.finance.entity.FeeInvoice;

public interface FeeInvoiceRepository extends JpaRepository<FeeInvoice, Long>, JpaSpecificationExecutor<FeeInvoice> {
	boolean existsByStudentIdAndFeeId(Long studentId, Long feeId);

	@Override
	@EntityGraph(attributePaths = { "fee", "student.user" })
	Page<FeeInvoice> findAll(Specification<FeeInvoice> spec, Pageable pageable);

	@Query("SELECT fi.student.id FROM FeeInvoice fi WHERE fi.fee.id = :feeId")
	Set<Long> findStudentIdsByFeeId(@Param("feeId") Long feeId);

	@Query("""
			    SELECT fi
			    FROM FeeInvoice fi
			    JOIN FETCH fi.fee f
			    JOIN FETCH f.academicYear ay
			    WHERE fi.student.id = :studentId
			      AND f.academicYear.id = :academicYearId
			      AND f.active = true
			    ORDER BY f.dueDate ASC, fi.id DESC
			""")
	List<FeeInvoice> findInvoicesByStudentIdAndAcademicYearId(
			@Param("studentId") Long studentId,
			@Param("academicYearId") Long academicYearId);

	@EntityGraph(attributePaths = { "fee.academicYear", "student.user" })
	Optional<FeeInvoice> findByIdAndStudentId(Long invoiceId, Long studentId);

	@EntityGraph(attributePaths = { "fee.academicYear", "student.user" })
	Optional<FeeInvoice> findById(Long invoiceId);

}