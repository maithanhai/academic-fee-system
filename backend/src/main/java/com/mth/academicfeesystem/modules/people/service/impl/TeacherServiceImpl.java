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
import com.mth.academicfeesystem.modules.people.dto.request.TeacherAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherListResponse;
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

import jakarta.persistence.criteria.JoinType;
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
    private final SubjectRepository subjectRepo;

    @Override
    public PageResponse<TeacherListResponse> searchTeachers(TeacherSearchRequest request, Pageable pageable) {
        Specification<Teacher> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("user", JoinType.LEFT);
                root.fetch("department", JoinType.LEFT);
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();
            var userJoin = root.join("user", JoinType.LEFT);
            var departmentJoin = root.join("department", JoinType.LEFT);
            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String searchPattern = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(userJoin.get("username")), searchPattern),
                        cb.like(cb.lower(userJoin.get("fullName")), searchPattern)));
            }
            if (request.getDepartmentId() != null) {
                predicates.add(cb.equal(departmentJoin.get("id"), request.getDepartmentId()));
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
    public TeacherDetailResponse updateTeacher(Long userId, TeacherAdminUpdateRequest request) {
        System.out.println("DATA từ request: " + request);
        Teacher teacher = teacherRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        User user = teacher.getUser();
        userMapper.toEntity(request, user);
        userRepo.save(user);

        if (request.getDepartmentId() != null && !request.getDepartmentId().equals(teacher.getDepartment().getId())) {
            Department department = departmentRepo.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            teacher.setDepartment(department);
            teacherRepo.save(teacher);
        }

        if (request.getSubjectIds() != null) {
            teacher.getExpertises().removeIf(exp -> !request.getSubjectIds().contains(exp.getSubject().getId()));
            List<Long> existingIds = teacher.getExpertises().stream()
                    .map(exp -> exp.getSubject().getId())
                    .toList();
            for (Long subjectId : request.getSubjectIds()) {
                if (!existingIds.contains(subjectId)) {
                    Subject subject = subjectRepo.findById(subjectId)
                            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

                    TeacherExpertise expertise = TeacherExpertise.builder()
                            .teacher(teacher)
                            .subject(subject)
                            .build();

                    teacher.getExpertises().add(expertise);
                }
            }
        }
        return teacherMapper.toDetailResponse(teacher);
    }
}
