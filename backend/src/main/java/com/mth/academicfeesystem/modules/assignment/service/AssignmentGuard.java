package com.mth.academicfeesystem.modules.assignment.service;

import org.springframework.stereotype.Component;

import com.mth.academicfeesystem.modules.assignment.repository.HomeroomAssignmentRepository;
import com.mth.academicfeesystem.modules.assignment.repository.TeachingAssignmentRepository;

import lombok.RequiredArgsConstructor;

@Component("assignmentGuard") 
@RequiredArgsConstructor
public class AssignmentGuard {

    private final HomeroomAssignmentRepository homeroomAssignmentRepo;
    private final TeachingAssignmentRepository teachingAssignmentRepo;

    //GVCN
    public boolean isHomeroomTeacher(Long teacherId, Long classId) {
        return homeroomAssignmentRepo.existsByTeacherIdAndSchoolClassId(teacherId, classId);
    }

    //GVBM(nhập điểm, xem điểm)
    public boolean canGradeSubject(Long teacherId, Long classId, Long subjectId) {
        return teachingAssignmentRepo.existsByTeacherIdAndSchoolClassIdAndSubjectId(teacherId, classId, subjectId);
    }

}