package com.mth.academicfeesystem.modules.people.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.people.dto.request.UpdateTeacherByAdminRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherResponse;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.entity.User;

@Mapper(componentModel = "spring",
    uses = {DepartmentMapper.class}
)
public interface TeacherMapper {
    @Mapping(target = "department",source = "department")
    TeacherDetailResponse toDetailResponse(Teacher teacher);
    //List students and paginator
    @Mapping(target = "username",source = "user.username")
    @Mapping(target = "fullName",source = "user.fullName")
    @Mapping(target = "active",source = "user.active")
    @Mapping(target = "department",source = "department")
    TeacherResponse toResponse(Teacher teacher);
    default PageResponse<TeacherResponse> toPageResponse(Page<Teacher> page) {
        if (page == null) {
            return null;
        }
        List<TeacherResponse> content = page.getContent()
                .stream()
                .map(this::toResponse) 
                .toList();
        return PageResponse.<TeacherResponse>builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(content) 
                .build();
    }

    @BeanMapping(ignoreByDefault = true)
    void registerToTeacher(RegisterTeacherRequest request,@MappingTarget Teacher teacher);
}
