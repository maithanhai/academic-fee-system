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
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.StudentAdminUpdateRequest;
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
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepo;

    @Override
    public PageResponse<StudentResponse> searchStudents(StudentSearchRequest request, Pageable pageable) {
        Specification<Student> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("user", JoinType.LEFT);
                root.fetch("cohort", JoinType.LEFT);
                query.distinct(true); // Tránh duplicate data khi join với collection (enrollments)
            }
            List<Predicate> predicates = new ArrayList<>();
            var userJoin = root.join("user", JoinType.LEFT);
            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String searchPattern = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(userJoin.get("username")), searchPattern),
                        cb.like(cb.lower(userJoin.get("fullName")), searchPattern)));
            }
            if (request.getActive() != null) {
                predicates.add(cb.equal(userJoin.get("active"), request.getActive()));
            }

            if (request.getClassId() != null || request.getGradeLevel() != null) {
                Join<Student, ClassEnrollment> enrollmentJoin = root.join("enrollments", JoinType.INNER);
                predicates.add(cb.equal(enrollmentJoin.get("status"), EnrollmentStatus.ACTIVE));
                if (request.getGradeLevel() != null) {
                    predicates.add(
                            cb.equal(enrollmentJoin.get("schoolClass").get("gradeLevel"), request.getGradeLevel()));
                }
                if (request.getClassId() != null) {
                    predicates.add(cb.equal(enrollmentJoin.get("schoolClass").get("id"), request.getClassId()));
                }
            }

            if (request.getCohortId() != null) {
                var cohortJoin = root.join("cohort", JoinType.LEFT);
                predicates.add(cb.equal(cohortJoin.get("id"), request.getCohortId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Student> studentPage = studentRepo.findAll(spec, pageable);
        return studentMapper.toPageResponse(studentPage);
    }

    @Override
    public StudentDetailResponse getStudentById(Long id) {
        Student student = studentRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found"));
        return studentMapper.toDetailResponse(student);
    }

    @Transactional
    @Override
    public StudentDetailResponse updateStudent(Long id, StudentAdminUpdateRequest request) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        User user = student.getUser();
        userMapper.toEntity(request, user);
        studentMapper.toEntity(request, student);

        userRepo.save(user);
        studentRepo.save(student);
        return studentMapper.toDetailResponse(student);
    }
}
