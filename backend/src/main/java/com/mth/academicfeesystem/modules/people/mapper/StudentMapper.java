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
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassListResponse;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.mapper.ClassEnrollmentMapper;
import com.mth.academicfeesystem.modules.academic.mapper.CohortMapper;
import com.mth.academicfeesystem.modules.people.dto.request.StudentProfileUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.request.StudentAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;


@Mapper(componentModel = "spring",
    uses = {ClassEnrollmentMapper.class,CohortMapper.class}
)

public interface StudentMapper {
    //Detail student
    @Mapping(target="id",source = "id")
    @Mapping(target="username",source = "user.username")
    @Mapping(target="fullName",source = "user.fullName")
    @Mapping(target="gender",source = "user.gender")
    @Mapping(target="email",source = "user.email")
    @Mapping(target="phone",source = "user.phone")
    @Mapping(target="createdDate",source = "user.createdDate")
    @Mapping(target="updatedDate",source = "user.updatedDate")
    @Mapping(target = "active",source = "user.active")
    @Mapping(target = "address",source = "address")
    @Mapping(target = "phoneParent",source = "phoneParent")
    @Mapping(target = "enrollments",source = "enrollments")
    @Mapping(target="dateOfBirth",source = "user.dateOfBirth")
    StudentDetailResponse toDetailResponse(Student student);

    //update student
    @Mapping(target="id",ignore = true)
    @Mapping(target="cohort",ignore=true)
    @Mapping(target="user",ignore=true)
    @Mapping(target = "enrollments",ignore = true)
    void toEntity(StudentProfileUpdateRequest request,@MappingTarget Student student);

    //List Students and Paginator
    @Mapping(target = "username",source = "user.username")
    @Mapping(target = "fullName",source = "user.fullName")
    @Mapping(target = "active",source = "user.active")
    @Mapping(target = "cohort",source = "cohort")
    @Mapping(target = "schoolClass",source = "enrollments",qualifiedByName = "extractActiveClassName")
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
    @Named("extractActiveClassName")
    default SchoolClassListResponse extractActiveClassName(List<ClassEnrollment> enrollments){
        if (enrollments == null || enrollments.isEmpty()) {
            return null;
        }
        
        return enrollments.stream()
                .filter(enrollment -> EnrollmentStatus.ACTIVE.equals(enrollment.getStatus()))
                .map(schoolClass -> schoolClass.getSchoolClass())
                .filter(schoolClass -> schoolClass != null)
                .map(schoolClass -> SchoolClassListResponse.builder().id(schoolClass.getId()).name(schoolClass.getName()).build())
                .findFirst()
                .orElse(null);
    }
    @BeanMapping(ignoreByDefault = true)
    void registerToStudent(RegisterStudentRequest request,@MappingTarget Student student);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "address",source = "request.address")
    @Mapping(target = "phoneParent",source = "request.phoneParent")
    void toEntity(StudentAdminUpdateRequest request,@MappingTarget Student student);

}
