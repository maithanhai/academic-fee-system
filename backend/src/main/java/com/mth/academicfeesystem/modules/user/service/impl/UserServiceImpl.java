package com.mth.academicfeesystem.modules.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.Role;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.modules.user.dto.request.ChangePasswordRequest;
import com.mth.academicfeesystem.modules.user.dto.request.UpdateProfileRequest;
import com.mth.academicfeesystem.modules.user.dto.response.MyProfileResponse;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.mapper.UserMapper;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;
import com.mth.academicfeesystem.modules.user.service.UserService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepo;
    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;
    private final UserMapper userMapper;
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
    public MyProfileResponse updateMyProfile(CustomUserPrincipal principal,UpdateProfileRequest request){
        User user = userRepo.findById(principal.getId()).orElseThrow(
            ()->new ResourceNotFoundException("Người dùng không tồn tại")
        );
        Student currStudent = null;
        Teacher currTeacher = null;
        if (principal.getRole()==Role.ROLE_TEACHER){
            currTeacher = teacherRepo.findById(principal.getId())
                .orElse(null);
            if (request.phone()!=null) user.setPhone(request.phone());
            if (request.email()!=null) user.setEmail(request.email());
        }
        else if (principal.getRole()==Role.ROLE_STUDENT){
            currStudent = studentRepo.findById(principal.getId())
                .orElse(null);
            if (request.phone()!=null) user.setPhone(request.phone());
            if (request.email()!=null) user.setEmail(request.email());
            if (request.phoneParent()!=null)currStudent.setPhoneParent(request.phoneParent());
            if(request.address()!=null)currStudent.setAddress(request.address());
            studentRepo.save(currStudent);
        }
        userRepo.save(user);
        return userMapper.toResponse(user, currStudent, currTeacher); 
    }

    @Transactional
    @Override
    public void changePassword(Long userId, ChangePasswordRequest request){
        User user = userRepo.findById(userId).orElseThrow(
            ()->new ResourceNotFoundException("Người dùng không tồn tại")
        );
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new BusinessException("Mật khẩu hiện tại không chính xác");
        }
        String newEncryptedPassword = passwordEncoder.encode(request.newPassword());
        user.setPassword(newEncryptedPassword);
        userRepo.save(user);
    }

    @Transactional
    @Override
    public void resetPassword(Long userId){
        User user = userRepo.findById(userId)
            .orElseThrow(()->new ResourceNotFoundException("Người dùng không tồn tại"));
        String password = String.valueOf(user.getDateOfBirth());
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }
}
