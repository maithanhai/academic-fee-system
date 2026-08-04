package com.mth.academicfeesystem.modules.assignment.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.assignment.repository.HomeroomAssignmentRepository;
import com.mth.academicfeesystem.modules.assignment.repository.TeachingAssignmentRepository;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component("assignmentGuard") 
@RequiredArgsConstructor
public class AssignmentGuard {

    private final HomeroomAssignmentRepository homeroomAssignmentRepo;
    private final TeachingAssignmentRepository teachingRepo;
    private final TeacherRepository teacherRepo;
    private final UserRepository userRepo;

    private Long getTeacherIdFromAuth(Authentication auth) {
        String username = auth.getName(); 
        User user = userRepo.findByUsername(username).orElseThrow(
            ()->new ResourceNotFoundException("Username không tìm thấy"));
        Teacher teacher = teacherRepo.findById(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên"));
        return teacher.getId();
    }

    //Kiem tra GVCN
    public boolean isHomeroomTeacher(Authentication auth, Long classId) {
        Long teacherId = getTeacherIdFromAuth(auth);
        
        return homeroomAssignmentRepo.findBySchoolClassIdAndStatus(classId, AssignmentStatus.ACTIVE)
            .map(assignment -> assignment.getTeacher().getId().equals(teacherId))
            .orElse(false); 
    }
    // Kiem tra GVBM
    public boolean isSubjectTeacher(Authentication auth, Long classId, Long subjectId) {
        Long teacherId = getTeacherIdFromAuth(auth);
        
        return teachingRepo.findBySchoolClassIdAndSubjectId(classId, subjectId)
            .map(assignment -> assignment.getTeacher().getId().equals(teacherId))
            .orElse(false);
    }
}