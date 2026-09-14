package com.mth.academicfeesystem.modules.people.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.modules.assignment.repository.TeachingAssignmentRepository;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherExpertiseResponse;
import com.mth.academicfeesystem.modules.people.dto.response.TeacherExpertiseResponse.TeacherResponse;
import com.mth.academicfeesystem.modules.people.entity.TeacherExpertise;
import com.mth.academicfeesystem.modules.people.repository.TeacherExpertiseRepository;
import com.mth.academicfeesystem.modules.people.service.TeacherExpertiseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherExpertiseServiceImpl implements TeacherExpertiseService {
    private final TeacherExpertiseRepository teacherExpertiseRepo;
    private final TeachingAssignmentRepository teachingAssignmentRepo;

    @Override
    public List<TeacherExpertiseResponse> getTeacherExpertisesWithWorkload(Long academicYearId){
        List<TeacherExpertise> teacherExpertises = teacherExpertiseRepo.findAll();
        Map<Long, Long> teacherWorkload = new HashMap<>();
        List<Object[]> dbWorkloads = teachingAssignmentRepo.countTeacherWorkloadByAcademicYear(academicYearId);
        for (Object[] row : dbWorkloads) {
            teacherWorkload.put((Long) row[0], (Long) row[1]); 
        }
        return teacherExpertises.stream().map(te->{
            Long teacherId = te.getTeacher().getId();
            return TeacherExpertiseResponse.builder()
                .subjectId(te.getSubject().getId())
                .teacher(TeacherResponse.builder()
                    .teacherId(teacherId)
                    .teacherName(te.getTeacher().getUser().getFullName())
                    .workload(teacherWorkload.getOrDefault(teacherId,0L))
                    .build())
                .build();
        }).toList();
    }
}
