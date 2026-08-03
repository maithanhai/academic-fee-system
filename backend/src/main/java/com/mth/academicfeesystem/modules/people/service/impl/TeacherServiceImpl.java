package com.mth.academicfeesystem.modules.people.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.UpdateTeacherByAdminRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherResponse;
import com.mth.academicfeesystem.modules.people.entity.Department;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.entity.TeacherExpertise;
import com.mth.academicfeesystem.modules.people.mapper.TeacherMapper;
import com.mth.academicfeesystem.modules.people.repository.DepartmentRepository;
import com.mth.academicfeesystem.modules.people.repository.TeacherExpertiseRepository;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.modules.people.service.TeacherService;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.mapper.UserMapper;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepo;
    private final TeacherMapper teacherMapper;
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final DepartmentRepository departmentRepo;
    private final TeacherExpertiseRepository teacherExpertiseRepo;
    private final SubjectRepository subjectRepo;

    @Override
    public PageResponse<TeacherResponse> searchTeachers(TeacherSearchRequest request, Pageable pageable) {
        Specification<Teacher> spec = (root, query, cb) -> {
            Join<Teacher, User> userJoin = root.join("user");
            Join<Teacher, Department> cohortJoin = root.join("department");
            List<Predicate> predicates = new ArrayList<>();

            if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(userJoin.get("username")),
                        "%" + request.getUsername().trim().toLowerCase() + "%"));
            }
            if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(userJoin.get("fullName")),
                        "%" + request.getFullName().trim().toLowerCase() + "%"));
            }
            if (request.getDepartment() != null && !request.getDepartment().isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(cohortJoin.get("name")), "%" + request.getDepartment().toLowerCase() + "%"));
            }
            if (request.getActive() != null) {
                predicates.add(cb.equal(userJoin.get("active"), request.getActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Teacher> teacherPage = teacherRepo.findAll(spec, pageable);
        return teacherMapper.toPageResponse(teacherPage);
    }

    @Override
    public TeacherDetailResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Teacher not found"));
        return teacherMapper.toDetailResponse(teacher);
    }

    @Transactional
    @Override
    public void updateTeacher(Long userId, UpdateTeacherByAdminRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Teacher teacher = teacherRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        userMapper.toEntity(request, user);
        userRepo.save(user);
        if (request.departmentId() != null && !request.departmentId().equals(teacher.getDepartment().getId())) {
            Department department = departmentRepo.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            teacher.setDepartment(department);
            teacherRepo.save(teacher);
        }

        if (request.subjectIds() != null && !request.subjectIds().isEmpty()) {
            teacherExpertiseRepo.deleteByTeacherId(userId);
            List<TeacherExpertise> newExpertises = new ArrayList<>();
            for (Long subjectId : request.subjectIds()) {
                Subject subject = subjectRepo.findById(subjectId)
                        .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

                TeacherExpertise expertise = TeacherExpertise.builder()
                        .teacher(teacher)
                        .subject(subject)
                        .build();

                newExpertises.add(expertise);
            }

            teacherExpertiseRepo.saveAll(newExpertises);
        }
    }
}
