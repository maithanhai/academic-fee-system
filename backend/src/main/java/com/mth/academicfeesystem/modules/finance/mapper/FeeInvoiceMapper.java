package com.mth.academicfeesystem.modules.finance.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.FeeInvoiceDetailResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.StudentInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.dto.response.TeacherInvoiceResponse;
import com.mth.academicfeesystem.modules.finance.entity.FeeInvoice;

@Mapper(componentModel = "spring")
public interface FeeInvoiceMapper {
	@Mapping(target = "studentName", source = "student.user.fullName")
	@Mapping(target = "feeName", source = "fee.name")
	@Mapping(target = "dueDate", source = "fee.dueDate")
	@Mapping(target = "invoiceId", source = "id")
	FeeInvoiceResponse toResponse(FeeInvoice invoice);

	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName", source = "student.user.fullName")
	@Mapping(target = "feeId", source = "fee.id")
	@Mapping(target = "feeName", source = "fee.name")
	@Mapping(target = "dueDate", source = "fee.dueDate")
	@Mapping(target = "actionById", source = "actionBy.id")
	@Mapping(target = "actionByName", source = "actionBy.fullName")
	FeeInvoiceDetailResponse toDetailResponse(FeeInvoice invoice);

	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName", source = "student.user.fullName")
	@Mapping(target = "className", expression = "java(activeClassName(invoice))")
	@Mapping(target = "feeId", source = "fee.id")
	@Mapping(target = "feeName", source = "fee.name")
	@Mapping(target = "dueDate", source = "fee.dueDate")
	TeacherInvoiceResponse toTeacherResponse(FeeInvoice invoice);

	@Mapping(target = "feeId", source = "fee.id")
	@Mapping(target = "feeName", source = "fee.name")
	@Mapping(target = "dueDate", source = "fee.dueDate")
	StudentInvoiceResponse toStudentResponse(FeeInvoice invoice);

	List<StudentInvoiceResponse> toListStudentResponses(List<FeeInvoice> feeInvoices);

	default PageResponse<FeeInvoiceResponse> toPageResponse(Page<FeeInvoice> page) {
		return toPage(page, this::toResponse);
	}

	default PageResponse<StudentInvoiceResponse> toStudentPageResponse(Page<FeeInvoice> page) {
		return toPage(page, this::toStudentResponse);
	}

	private <T> PageResponse<T> toPage(Page<FeeInvoice> page, java.util.function.Function<FeeInvoice, T> mapper) {
		if (page == null) return null;
		List<T> data = page.getContent().stream().map(mapper).toList();
		return PageResponse.<T>builder()
				.currentPage(page.getNumber() + 1)
				.pageSize(page.getSize())
				.totalPages(page.getTotalPages())
				.totalElements(page.getTotalElements())
				.data(data)
				.build();
	}

	default String activeClassName(FeeInvoice invoice) {
		if (invoice.getStudent() == null || invoice.getStudent().getEnrollments() == null) return null;
		return invoice.getStudent().getEnrollments().stream()
				.filter(e -> com.mth.academicfeesystem.common.enums.EnrollmentStatus.ACTIVE.equals(e.getStatus()))
				.map(e -> e.getSchoolClass() != null ? e.getSchoolClass().getName() : null)
				.filter(java.util.Objects::nonNull)
				.findFirst().orElse(null);
	}

}
