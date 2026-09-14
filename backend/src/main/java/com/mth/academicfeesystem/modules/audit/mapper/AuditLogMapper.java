package com.mth.academicfeesystem.modules.audit.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.GradeAuditLogResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogDetailResponse;
import com.mth.academicfeesystem.modules.audit.dto.response.InvoiceAuditLogResponse;
import com.mth.academicfeesystem.modules.audit.entity.AuditLog;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
	@Mapping(target = "actorId", source = "user.id")
	@Mapping(target = "actorFullName", source = "user.fullName")
	GradeAuditLogResponse toGradeResponse(AuditLog auditLog);

	@Mapping(target = "actorId", source = "user.id")
	@Mapping(target = "actorFullName", source = "user.fullName")
	GradeAuditLogDetailResponse toGradeDetailResponse(AuditLog auditLog);

	@Mapping(target = "actorId", source = "user.id")
	@Mapping(target = "actorFullName", source = "user.fullName")
	InvoiceAuditLogResponse toInvoiceResponse(AuditLog auditLog);

	@Mapping(target = "actorId", source = "user.id")
	@Mapping(target = "actorFullName", source = "user.fullName")
	InvoiceAuditLogDetailResponse toInvoiceDetailResponse(AuditLog auditLog);

	default PageResponse<GradeAuditLogResponse> toGradePageResponse(Page<AuditLog> page) {
		if (page == null) {
			return null;
		}
		List<GradeAuditLogResponse> content = page.getContent()
				.stream()
				.map(this::toGradeResponse)
				.toList();
		return PageResponse.<GradeAuditLogResponse>builder()
				.currentPage(page.getNumber() + 1)
				.pageSize(page.getSize())
				.totalPages(page.getTotalPages())
				.totalElements(page.getTotalElements())
				.data(content)
				.build();
	}

	default PageResponse<InvoiceAuditLogResponse> toInvoicePageResponse(Page<AuditLog> page) {
		if (page == null) {
			return null;
		}
		List<InvoiceAuditLogResponse> content = page.getContent()
				.stream()
				.map(this::toInvoiceResponse)
				.toList();
		return PageResponse.<InvoiceAuditLogResponse>builder()
				.currentPage(page.getNumber() + 1)
				.pageSize(page.getSize())
				.totalPages(page.getTotalPages())
				.totalElements(page.getTotalElements())
				.data(content)
				.build();
	}

}
