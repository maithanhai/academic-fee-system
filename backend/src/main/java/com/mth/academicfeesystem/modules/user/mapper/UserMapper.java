package com.mth.academicfeesystem.modules.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.mth.academicfeesystem.modules.people.dto.request.UpdateStudentByAdminRequest;
import com.mth.academicfeesystem.modules.people.dto.request.UpdateTeacherByAdminRequest;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.mapper.StudentMapper;
import com.mth.academicfeesystem.modules.people.mapper.TeacherMapper;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.dto.request.UpdateProfileRequest;
import com.mth.academicfeesystem.modules.user.dto.response.MyProfileResponse;
import com.mth.academicfeesystem.modules.user.entity.User;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {StudentMapper.class,TeacherMapper.class})
public interface UserMapper {
    @Mapping(source = "student",target = "studentDetailResponse")
    @Mapping(source="teacher",target = "teacherDetailResponse")
    MyProfileResponse toResponse(User user, Student student, Teacher teacher);
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email",source = "email")
    @Mapping(target = "phone",source="phone")
    void toEntity(UpdateProfileRequest request, @MappingTarget User user);
    
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fullName",source = "fullName")
    @Mapping(target="gender",source = "gender")
    @Mapping(target = "dateOfBirth",source="dateOfBirth")
    void registerStudentToUser(RegisterStudentRequest request,@MappingTarget User user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fullName",source = "fullName")
    @Mapping(target = "gender",source = "gender")
    @Mapping(target = "dateOfBirth",source = "dateOfBirth")
    @Mapping(target = "phone",source = "phone")
    @Mapping(target = "email", source = "email")
    void registerTeacherToUser(RegisterTeacherRequest request,@MappingTarget User user);

    @BeanMapping(ignoreByDefault = true)
    void toEntity(UpdateTeacherByAdminRequest request,@MappingTarget User user);
    
    @BeanMapping(ignoreByDefault = true)
    void toEntity(UpdateStudentByAdminRequest request,@MappingTarget User user);


}
