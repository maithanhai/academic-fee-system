package com.mth.academicfeesystem.modules.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.mapper.StudentMapper;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.modules.user.dto.request.ChangeActiveRequest;
import com.mth.academicfeesystem.modules.user.dto.request.ChangePasswordRequest;
import com.mth.academicfeesystem.modules.user.dto.request.UpdateProfileRequest;
import com.mth.academicfeesystem.modules.user.dto.response.MyProfileResponse;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.mapper.UserMapper;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;
import com.mth.academicfeesystem.modules.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepo;
    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;
    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public MyProfileResponse getMyProfile(Long userId){
        User user=userRepo.findById(userId).orElseThrow(
            ()->new ResourceNotFoundException("Không tìm thấy tài khoản")
        );
        Student student=studentRepo.findById(userId).orElse(null);
        Teacher teacher=teacherRepo.findById(userId).orElse(null);
        return userMapper.toResponse(user, student, teacher);
    }
    @Transactional
    @Override
    public MyProfileResponse updateMyProfile(Long userId,UpdateProfileRequest request){
        User user = userRepo.findById(userId).orElseThrow(
            ()->new ResourceNotFoundException("User not found")
        );
        userMapper.toEntity(request, user);
        userRepo.save(user);
        Student currentStudent = studentRepo.findById(userId).orElse(null);
        if (request.student()!=null&&currentStudent!=null){
            studentMapper.toEntity(request.student(), currentStudent);
            studentRepo.save(currentStudent);
        }
        Teacher currentTeacher = teacherRepo.findById(userId).orElse(null);
        return userMapper.toResponse(user, currentStudent, currentTeacher);
    }

    @Transactional
    @Override
    public void changePassword(Long userId, ChangePasswordRequest request){
        User user = userRepo.findById(userId).orElseThrow(
            ()->new ResourceNotFoundException("User not found")
        );
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new BusinessException("Password is not valid");
        }
        String newEncryptedPassword = passwordEncoder.encode(request.newPassword());
        user.setPassword(newEncryptedPassword);
        userRepo.save(user);
    }

    @Transactional
    @Override
    public void changeActive(Long userId, ChangeActiveRequest request){
        User user = userRepo.findById(userId).orElseThrow(
            ()-> new ResourceNotFoundException("User not found")
        );
        if (user.isActive()!=request.active()){
            user.setActive(request.active());
            userRepo.save(user);
        }
    }
}
