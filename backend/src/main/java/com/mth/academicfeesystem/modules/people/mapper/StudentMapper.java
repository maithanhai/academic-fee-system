package com.mth.academicfeesystem.modules.people.mapper;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.mapper.ClassEnrollmentMapper;
import com.mth.academicfeesystem.modules.academic.mapper.CohortMapper;
import com.mth.academicfeesystem.modules.people.dto.request.StudentRequest;
import com.mth.academicfeesystem.modules.people.dto.request.UpdateStudentByAdminRequest;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;

@Mapper(componentModel = "spring",
    uses = {ClassEnrollmentMapper.class,CohortMapper.class}
)
public interface StudentMapper {
    //Detail student
    @Mapping(target = "address",source = "address")
    @Mapping(target = "phoneParent",source = "phoneParent")
    @Mapping(target = "currentClassName",source = "enrollments",qualifiedByName = "getActiveClassName")
    @Mapping(target = "enrollments",source = "enrollments")
    StudentDetailResponse toDetailResponse(Student student);

    //update student
    @Mapping(target="id",ignore = true)
    @Mapping(target="cohort",ignore=true)
    @Mapping(target="user",ignore=true)
    @Mapping(target = "enrollments",ignore = true)
    void toEntity(StudentRequest request,@MappingTarget Student student);

    @Named("getActiveClassName")
    default String getActiveClassName(List<ClassEnrollment> enrollments) {
        if (enrollments == null || enrollments.isEmpty()) {
            return null;
        }
        return enrollments.stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.ACTIVE)
                .map(enrollment -> enrollment.getSchoolClass().getName())
                .findFirst()
                .orElse(null); 
    }
    //List Students and Paginator
    @Mapping(target = "username",source = "user.username")
    @Mapping(target = "fullName",source = "user.fullName")
    @Mapping(target = "active",source = "user.active")
    @Mapping(target = "cohort",source = "cohort.name")
    StudentResponse toResponse(Student student);
    default PageResponse<StudentResponse> toPageResponse(Page<Student> page) {
        if (page == null) {
            return null;
        }
        List<StudentResponse> content = page.getContent()
                .stream()
                .map(this::toResponse) 
                .toList();
        return PageResponse.<StudentResponse>builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(content) 
                .build();
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "address", source = "address")
    void registerToStudent(RegisterStudentRequest request,@MappingTarget Student student);

    @BeanMapping(ignoreByDefault = true)
    void toEntity(UpdateStudentByAdminRequest request,@MappingTarget Student student);

}
