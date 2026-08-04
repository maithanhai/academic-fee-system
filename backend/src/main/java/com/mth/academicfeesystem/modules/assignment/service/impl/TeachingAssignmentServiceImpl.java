package com.mth.academicfeesystem.modules.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkTeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.request.TeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.entity.TeachingAssignment;
import com.mth.academicfeesystem.modules.assignment.repository.TeachingAssignmentRepository;
import com.mth.academicfeesystem.modules.assignment.service.TeachingAssignmentService;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeachingAssignmentServiceImpl implements TeachingAssignmentService {
    private final TeachingAssignmentRepository teachingAssignmentRepo;
    private final TeacherRepository teacherRepo;
    private final SchoolClassRepository classRepo;
    private final SubjectRepository subjectRepo;

    @Override
    public List<TeachingAssignmentResponse> previewAutoAssign(Integer gradeLevel) {
        List<TeachingAssignmentResponse> previewList = new ArrayList<>();
        List<SchoolClass> schoolClasses = classRepo.findByGradeLevel(gradeLevel);
        List<Subject> subjects = subjectRepo.findAll();

        for (Subject subject : subjects) {
            List<Teacher> availableTeachers = teacherRepo.findTeachersBySubjectId(subject.getId());

            if (availableTeachers.isEmpty()) {
                for (SchoolClass schoolClass : schoolClasses) {
                    previewList.add(new TeachingAssignmentResponse(schoolClass.getId(), subject.getId(), null, "Chưa có giáo viên"));
                }
                continue;
            }

            int teacherIndex = 0;
            for (SchoolClass schoolClass : schoolClasses) {
                Teacher assignedTeacher = availableTeachers.get(teacherIndex % availableTeachers.size());

                previewList.add(new TeachingAssignmentResponse(
                        schoolClass.getId(),
                        subject.getId(),
                        assignedTeacher.getId(),
                        assignedTeacher.getUser().getFullName()));

                teacherIndex++;
            }
        }

        return previewList;
    }

    @Transactional
    @Override
    public void bulkSaveAssignments(BulkTeachingAssignmentRequest request) {
        for (TeachingAssignmentRequest item : request.assignments()) {

            if (item.teacherId() == null)
                continue;

            SchoolClass schoolClass = classRepo.findById(item.classId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học"));
            Subject subject = subjectRepo.findById(item.subjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));
            Teacher teacher = teacherRepo.findById(item.teacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên"));

            Optional<TeachingAssignment> existingOpt = teachingAssignmentRepo
                    .findBySchoolClassIdAndSubjectId(schoolClass.getId(), subject.getId());

            if (existingOpt.isPresent()) {
                TeachingAssignment existing = existingOpt.get();
                if (!existing.getTeacher().getId().equals(teacher.getId())) {
                    existing.setTeacher(teacher);
                    teachingAssignmentRepo.save(existing);
                }
            } else {
                TeachingAssignment newAssignment = TeachingAssignment.builder()
                        .schoolClass(schoolClass)
                        .subject(subject)
                        .teacher(teacher)
                        .build();
                teachingAssignmentRepo.save(newAssignment);
            }
        }
    }

}
