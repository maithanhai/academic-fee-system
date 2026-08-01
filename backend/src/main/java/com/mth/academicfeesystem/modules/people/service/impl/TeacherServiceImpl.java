package com.mth.academicfeesystem.modules.people.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.people.dto.request.TeacherSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherResponse;
import com.mth.academicfeesystem.modules.people.entity.Department;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.mapper.TeacherMapper;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.modules.people.service.TeacherService;
import com.mth.academicfeesystem.modules.user.entity.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{
    private final TeacherRepository teacherRepo;
    private final TeacherMapper teacherMapper;
    @Override
    public PageResponse<TeacherResponse> searchTeachers(TeacherSearchRequest request, Pageable pageable){
        Specification<Teacher> spec = (root, query, cb) -> {
            Join<Teacher, User> userJoin = root.join("user");
            Join<Teacher, Department> cohortJoin = root.join("department");
            List<Predicate> predicates = new ArrayList<>();
            
            if (request.getUsername()!=null&&!request.getUsername().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(userJoin.get("username")),
                 "%" + request.getUsername().trim().toLowerCase() + "%"));
            }
            if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(userJoin.get("fullName")),
                 "%" + request.getFullName().trim().toLowerCase() + "%"));
            }
            if (request.getDepartment() != null && !request.getDepartment().isEmpty()) {
                predicates.add(cb.like(cb.lower(cohortJoin.get("name")), "%" + request.getDepartment().toLowerCase() + "%"));
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
    public TeacherDetailResponse getTeacherById(Long id){
        Teacher teacher = teacherRepo.findById(id).orElseThrow(
            ()->new ResourceNotFoundException("Teacher not found")
        );
        return teacherMapper.toDetailResponse(teacher);
    }
}
