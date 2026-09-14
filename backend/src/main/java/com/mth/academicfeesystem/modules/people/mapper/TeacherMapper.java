package com.mth.academicfeesystem.modules.people.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherListResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherResponse;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.entity.TeacherExpertise;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
@Mapper(componentModel = "spring",
    uses = {DepartmentMapper.class}
)
public interface TeacherMapper {
    @Mapping(target = "username",source = "user.username")
    @Mapping(target = "fullName",source = "user.fullName")
    @Mapping(target = "email",source = "user.email")
    @Mapping(target = "phone",source = "user.phone")
    @Mapping(target = "gender",source = "user.gender")
    @Mapping(target = "createdDate",source = "user.createdDate")
    @Mapping(target = "updatedDate",source = "user.updatedDate")
    @Mapping(target = "active",source = "user.active")
    @Mapping(target = "department",source = "department")
    @Mapping(target = "dateOfBirth",source = "user.dateOfBirth")
    @Mapping(target = "subjects",source = "expertises",qualifiedByName = "mapExpertisesToSubjects")
    TeacherDetailResponse toDetailResponse(Teacher teacher);
    @Named("mapExpertisesToSubjects")
    default List<SubjectResponse> mapExpertisesToSubjects(List<TeacherExpertise> expertises) {
        if (expertises == null || expertises.isEmpty()) {
            return Collections.emptyList(); 
        }
        
        return expertises.stream()
                .map(exp -> SubjectResponse.builder()
                                .id(exp.getSubject().getId())
                                .name(exp.getSubject().getName())
                                .active(exp.getSubject().getActive())
                                .build()
                )
                .toList();
    }
    //List students and paginator
    @Mapping(target = "username",source = "user.username")
    @Mapping(target = "fullName",source = "user.fullName")
    @Mapping(target = "active",source = "user.active")
    @Mapping(target = "department",source = "department")
    TeacherListResponse toListResponse(Teacher teacher);
    default PageResponse<TeacherListResponse> toPageResponse(Page<Teacher> page) {
        if (page == null) {
            return null;
        }
        List<TeacherListResponse> content = page.getContent()
                .stream()
                .map(this::toListResponse) 
                .toList();
        return PageResponse.<TeacherListResponse>builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(content) 
                .build();
    }

    @BeanMapping(ignoreByDefault = true)
    void registerToTeacher(RegisterTeacherRequest request,@MappingTarget Teacher teacher);

    @Mapping(target = "teacherName", source = "user.fullName")
    TeacherResponse toResponse(Teacher teacher);
    List<TeacherResponse> toListResponses(List<Teacher> teachers);
}
