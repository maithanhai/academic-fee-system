package com.mth.academicfeesystem.modules.people.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.UpdateStudentByAdminRequest;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.mapper.StudentMapper;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.modules.people.service.StudentService;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.mapper.UserMapper;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepo;
    
    @Override
    public PageResponse<StudentResponse> searchStudents(StudentSearchRequest request, Pageable pageable) {
        Specification<Student> spec = (root, query, cb) -> {
            Join<Student, User> userJoin = root.join("user");
            Join<Student, Cohort> cohortJoin = root.join("cohort");
            List<Predicate> predicates = new ArrayList<>();
            
            if (request.getUsername()!=null&&!request.getUsername().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(userJoin.get("username")),
                 "%" + request.getUsername().trim().toLowerCase() + "%"));
            }
            if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(userJoin.get("fullName")),
                 "%" + request.getFullName().trim().toLowerCase() + "%"));
            }
            if (request.getCohort() != null && !request.getCohort().isEmpty()) {
                predicates.add(cb.like(cb.lower(cohortJoin.get("name")), "%" + request.getCohort().toLowerCase() + "%"));
            }
            if (request.getActive() != null) {
            predicates.add(cb.equal(userJoin.get("active"), request.getActive()));
            }

            if (request.getClassId() != null) {
            Join<Student, ClassEnrollment> enrollmentJoin = root.join("enrollments");
            
            predicates.add(cb.equal(enrollmentJoin.get("schoolClass").get("id"), request.getClassId()));
            predicates.add(cb.equal(enrollmentJoin.get("status"), EnrollmentStatus.ACTIVE));
        }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
            Page<Student> studentPage = studentRepo.findAll(spec, pageable);
        return studentMapper.toPageResponse(studentPage);
    }

    @Override
    public StudentDetailResponse getStudentById(Long id){
        Student student = studentRepo.findById(id).orElseThrow(
            ()->new ResourceNotFoundException("Student not found")
        );
        return studentMapper.toDetailResponse(student);
    }

    @Transactional
    @Override
    public void updateStudent(Long id, UpdateStudentByAdminRequest request){
        User user = userRepo.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("User not found"));
        Student student = studentRepo.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Student not found"));
        userMapper.toEntity(request, user);
        studentMapper.toEntity(request, student);

        userRepo.save(user);
        studentRepo.save(student);
    }
}
