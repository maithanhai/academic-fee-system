package com.mth.academicfeesystem.modules.assignment.service;

import org.springframework.stereotype.Component;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.modules.assignment.repository.HomeroomAssignmentRepository;
import com.mth.academicfeesystem.modules.assignment.repository.TeachingAssignmentRepository;

import lombok.RequiredArgsConstructor;

@Component("assignmentGuard") 
@RequiredArgsConstructor
public class AssignmentGuard {

    private final HomeroomAssignmentRepository homeroomAssignmentRepo;
    private final TeachingAssignmentRepository teachingRepo;

    //GVCN
    public boolean isHomeroomTeacher(Long teacherId, Long classId) {
        return homeroomAssignmentRepo.existsByTeacherIdAndSchoolClassIdAndStatus(teacherId, classId, AssignmentStatus.ACTIVE);
    }

    //GVBM
    public boolean isSubjectTeacher(Long teacherId, Long classId, Long subjectId) {
        return teachingRepo.existsByTeacherIdAndSchoolClassIdAndSubjectId(teacherId, classId, subjectId);
    }
}