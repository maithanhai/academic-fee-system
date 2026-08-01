package com.mth.academicfeesystem.modules.user.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.repository.ClassEnrollmentRepository;
import com.mth.academicfeesystem.modules.academic.repository.CohortRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.people.entity.Department;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.mapper.StudentMapper;
import com.mth.academicfeesystem.modules.people.repository.DepartmentRepository;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.modules.user.dto.request.LoginRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RefreshTokenRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterTeacherRequest;
import com.mth.academicfeesystem.modules.user.dto.response.LoginResponse;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.mapper.UserMapper;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;
import com.mth.academicfeesystem.modules.user.service.AuthService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;
import com.mth.academicfeesystem.security.JwtTokenProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final CohortRepository cohortRepo;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepo;
    private final SchoolClassRepository schoolClassRepo;
    private final ClassEnrollmentRepository classEnrollmentRepo;
    private final DepartmentRepository departmentRepo;
    private final TeacherRepository teacherRepo;
    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication;
        //authenticate ném ngoại lệ nếu xảy ra lỗi
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        } catch (BadCredentialsException e) {
            throw new BusinessException("Username or password is not valid");
        }catch (DisabledException | LockedException e) {
            throw new BusinessException("Account is blocked");
        }
        CustomUserPrincipal userPrincipal=(CustomUserPrincipal)authentication.getPrincipal();
        Long userId=userPrincipal.getId();
        String username=userPrincipal.getUsername();
        String fullName=userPrincipal.getUser().getFullName();
        String role=userPrincipal.getUser().getRole().name();
        String accessToken=jwtTokenProvider.generateAccessToken(userId, username, role);
        String refreshToken=jwtTokenProvider.generateRefreshToken(userId, username);
        return new LoginResponse(accessToken,refreshToken,"Bearer",userId,username,fullName,role);
    }

    @Transactional
    @Override
    public void registerStudent(RegisterStudentRequest request){
        User user = new User();
        userMapper.registerStudentToUser(request, user);
        user.setUsername(UUID.randomUUID().toString());
        userRepo.save(user);
        Long userId=user.getId();
        Cohort cohort = cohortRepo.findById(request.cohortId())
            .orElseThrow(() -> new RuntimeException("Cohort not found"));
        String username = "hsk"+cohort.getName()+String.format("%05d", userId.intValue());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(String.valueOf(request.dateOfBirth())));
        userRepo.save(user);
        Student student=new Student();
        studentMapper.registerToStudent(request,student);
        student.setCohort(cohort);
        student.setUser(user);
        studentRepo.save(student);

        SchoolClass schoolClass=schoolClassRepo.findById(request.schoolClassId()).orElseThrow(
            ()->new ResourceNotFoundException("Class not found")
        );

        ClassEnrollment classEnrollment = ClassEnrollment.builder()
            .student(student)
            .schoolClass(schoolClass)
            .build();
        classEnrollmentRepo.save(classEnrollment);
    }

    @Transactional
    @Override
    public void registerTeacher(RegisterTeacherRequest request){
        User user = new User();
        userMapper.registerTeacherToUser(request, user);
        LocalDate date = LocalDate.now();
        String[] nameParts = request.fullName().trim().split("\\s+");
        StringBuilder firstLetterBuilder = new StringBuilder();
        for (String part : nameParts) {
            firstLetterBuilder.append(part.charAt(0));
        }
        String firstLetter = firstLetterBuilder.toString().toLowerCase();
        String username = "gv" + String.valueOf(date).concat("/") + firstLetter;
    
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(String.valueOf(request.dateOfBirth())));  
        userRepo.save(user);  
        Department department = departmentRepo.findById(request.departmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Teacher teacher = new Teacher();
        teacher.setDepartment(department);
        teacher.setUser(user); 
        teacherRepo.save(teacher);
    }

    @Transactional
    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request){
        String refreshToken = request.refreshToken();
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException("Invalid refresh token");
        }
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());
        return LoginResponse.builder()
          .accessToken(newAccessToken)
          .refreshToken(newRefreshToken)
          .userId(user.getId())
          .username(user.getUsername())
          .role(user.getRole().name())
          .build();
 }

