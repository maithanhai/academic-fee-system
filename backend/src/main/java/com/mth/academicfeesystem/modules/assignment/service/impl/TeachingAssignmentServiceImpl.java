package com.mth.academicfeesystem.modules.assignment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkTeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkTeachingAssignmentRequest.TeachingAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.dto.response.TeachingClassResponse;
import com.mth.academicfeesystem.modules.assignment.entity.TeachingAssignment;
import com.mth.academicfeesystem.modules.assignment.repository.TeachingAssignmentRepository;
import com.mth.academicfeesystem.modules.assignment.service.TeachingAssignmentService;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeachingAssignmentServiceImpl implements TeachingAssignmentService {
    private final TeachingAssignmentRepository teachingAssignmentRepo;
    private final TeacherRepository teacherRepo;
    private final SchoolClassRepository classRepo;
    private final SubjectRepository subjectRepo;
    private final AcademicYearRepository academicYearRepo;

    @Override
    public List<TeachingAssignmentResponse> getExistingAssignments(Long academicYearId, List<Integer> gradeLevels) {
        if (gradeLevels == null || gradeLevels.isEmpty())
            throw new BusinessException("Danh sách khối lớp không để trống");
        boolean hasInvalidGrade = gradeLevels.stream().anyMatch(gl -> gl < 10 || gl > 12);
        if (hasInvalidGrade) {
            throw new BusinessException("Khối lớp không hợp lệ, chỉ áp dụng cho khối 10, 11 , 12");
        }
        Map<Long, Long> teacherWorkload = new HashMap<>();
        List<Object[]> dbWorkloads = teachingAssignmentRepo.countTeacherWorkloadByAcademicYear(academicYearId);
        for (Object[] row : dbWorkloads) {
            teacherWorkload.put((Long) row[0], (Long) row[1]);
        }
        List<TeachingAssignment> existingAssignments = teachingAssignmentRepo
                .findByAcademicYearAndGradeLevels(academicYearId, gradeLevels);
        return existingAssignments.stream().map(assignment -> {
            Long teacherId = assignment.getTeacher().getId();
            return TeachingAssignmentResponse.builder()
                    .classId(assignment.getSchoolClass().getId())
                    .subjectId(assignment.getSubject().getId())
                    .teacherId(teacherId)
                    .teacherName(assignment.getTeacher().getUser().getFullName())
                    .workload(teacherWorkload.getOrDefault(teacherId, 0L))
                    .build();
        }).toList();

    }

    @Override
    public List<TeachingAssignmentResponse> copyAssignmentsPreviousYear(Long academicYearId,
            List<Integer> gradeLevels) {
        AcademicYear academicYear = academicYearRepo.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
        if (!academicYear.getActive())
            throw new BusinessException("Năm học đã đóng");
        if (gradeLevels == null || gradeLevels.isEmpty())
            throw new BusinessException("Danh sách khối lớp không để trống");

        boolean hasInvalidGrade = gradeLevels.stream().anyMatch(gl -> gl < 10 || gl > 12);
        if (hasInvalidGrade) {
            throw new BusinessException("Khối lớp không hợp lệ, chỉ áp dụng cho khối 10, 11 , 12");
        }
        AcademicYear previousYear = academicYearRepo.findById(academicYear.getId() - 1)
                .orElseThrow(() -> new BusinessException("Dữ liệu năm học trước không tồn tại"));

        List<TeachingAssignment> oldAssignments = teachingAssignmentRepo
                .findByAcademicYearAndGradeLevels(previousYear.getId(), gradeLevels);
        if (oldAssignments.isEmpty()) {
            throw new BusinessException("Dữ liệu phân công năm trước không tồn tại");
        }
        List<SchoolClass> currentClasses = classRepo.findByAcademicYearIdAndGradeLevelIn(academicYearId, gradeLevels);
        Map<String, Long> currentClassMapByName = currentClasses.stream()
                .collect(Collectors.toMap(SchoolClass::getName, SchoolClass::getId));
        Map<Long, Long> teacherWorkload = new HashMap<>();
        List<Object[]> dbWorkloads = teachingAssignmentRepo.countTeacherWorkloadByAcademicYear(academicYearId);
        for (Object[] row : dbWorkloads) {
            teacherWorkload.put((Long) row[0], (Long) row[1]);
        }
        List<TeachingAssignmentResponse> result = new ArrayList<>();
        for (TeachingAssignment oldAssign : oldAssignments) {
            String className = oldAssign.getSchoolClass().getName();
            Long newClassId = currentClassMapByName.get(className);
            if (newClassId != null) {
                Long teacherId = oldAssign.getTeacher().getId();
                result.add(TeachingAssignmentResponse.builder()
                        .classId(newClassId)
                        .subjectId(oldAssign.getSubject().getId())
                        .teacherId(teacherId)
                        .teacherName(oldAssign.getTeacher().getUser().getFullName())
                        .workload(teacherWorkload.getOrDefault(teacherId, 0L))
                        .build());
            }
        }
        return result;
    }

    @Override
    public List<TeachingAssignmentResponse> previewAutoAssign(Long academicYearId, List<Integer> gradeLevels) {
        AcademicYear academicYear = academicYearRepo.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
        if (!academicYear.getActive())
            throw new BusinessException("Năm học đã đóng");
        if (gradeLevels == null || gradeLevels.isEmpty())
            throw new BusinessException("Danh sách khối lớp không để trống");
        boolean hasInvalidGrade = gradeLevels.stream().anyMatch(gl -> gl < 10 || gl > 12);
        if (hasInvalidGrade) {
            throw new BusinessException("Khối lớp không hợp lệ, chỉ áp dụng cho khối 10, 11 , 12");
        }
        List<SchoolClass> schoolClasses = classRepo.findByAcademicYearIdAndGradeLevelIn(academicYearId, gradeLevels);
        if (schoolClasses.isEmpty()) {
            throw new BusinessException("Không tìm thấy lớp học nào cho khối và năm học này");
        }
        List<Subject> subjects = subjectRepo.findAll();
        Map<Long, Long> teacherWorkload = new HashMap<>();
        List<Object[]> dbWorkloads = teachingAssignmentRepo.countTeacherWorkloadByAcademicYear(academicYearId);
        for (Object[] row : dbWorkloads) {
            teacherWorkload.put((Long) row[0], (Long) row[1]);
        }
        Map<Long, List<Teacher>> availableTeachersMap = new HashMap<>();
        List<Object[]> teacherMappings = teacherRepo.findAllTeacherSubjectMappings();
        for (Object[] row : teacherMappings) {
            Long subId = (Long) row[0];
            Teacher teacher = (Teacher) row[1];
            availableTeachersMap.computeIfAbsent(subId, k -> new ArrayList<>()).add(teacher);
        }
        List<TeachingAssignmentResponse> previewList = new ArrayList<>();
        for (Subject subject : subjects) {
            List<Teacher> availableTeachers = availableTeachersMap.getOrDefault(subject.getId(), new ArrayList<>());

            if (availableTeachers.isEmpty()) {
                for (SchoolClass schoolClass : schoolClasses) {
                    previewList.add(TeachingAssignmentResponse.builder()
                            .classId(schoolClass.getId())
                            .subjectId(subject.getId())
                            .build());
                }
                continue;
            }
            for (SchoolClass schoolClass : schoolClasses) {
                Teacher selectedTeacher = availableTeachers.get(0);
                long minWorkload = Long.MAX_VALUE;
                for (Teacher teacher : availableTeachers) {
                    long currentWorkload = teacherWorkload.getOrDefault(teacher.getId(), 0L);
                    if (currentWorkload < minWorkload) {
                        minWorkload = currentWorkload;
                        selectedTeacher = teacher;
                    }
                }
                long newWorkload = minWorkload + 1;
                previewList.add(TeachingAssignmentResponse.builder()
                        .classId(schoolClass.getId())
                        .subjectId(subject.getId())
                        .teacherId(selectedTeacher.getId())
                        .teacherName(selectedTeacher.getUser().getFullName())
                        .workload(newWorkload)
                        .build());
                teacherWorkload.put(selectedTeacher.getId(), newWorkload);
            }
        }
        return previewList;
    }

    @Transactional
    @Override
    public void bulkSaveAssignments(BulkTeachingAssignmentRequest request) {
        if (request.assignments() == null || request.assignments().isEmpty())
            return;
        AcademicYear academicYear = academicYearRepo.findById(request.academicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
        if (!academicYear.getActive())
            throw new BusinessException("Năm học đã đóng");
        List<Long> classIds = request.assignments().stream()
                .map(req -> req.classId()).distinct()
                .toList();
        List<Long> subjectIds = request.assignments().stream()
                .map(req -> req.subjectId()).distinct()
                .toList();
        List<Long> teacherIds = request.assignments().stream()
                .map(req -> req.teacherId())
                .filter(id -> id != null)
                .distinct().toList();

        Map<Long, SchoolClass> classMap = classRepo.findAllById(classIds).stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c));
        for (Long classId : classIds) {
            SchoolClass schoolClass = classMap.get(classId);
            if (schoolClass == null) {
                throw new ResourceNotFoundException("Lớp học không tồn tại: " + classId);
            }
            if (!schoolClass.getAcademicYear().getId().equals(academicYear.getId())) {
                throw new BusinessException(
                        "Lớp " + schoolClass.getName() + " không thuộc năm học này!");
            }
        }
        Map<Long, Subject> subjectMap = subjectRepo.findAllById(subjectIds).stream()
                .collect(Collectors.toMap(s -> s.getId(), s -> s));
        Map<Long, Teacher> teacherMap = teacherRepo.findAllById(teacherIds).stream()
                .collect(Collectors.toMap(t -> t.getId(), t -> t));
        List<TeachingAssignment> existingAssignments = teachingAssignmentRepo.findBySchoolClassIdIn(classIds);
        Map<String, TeachingAssignment> existingMap = existingAssignments.stream()
                .collect(Collectors.toMap(a -> a.getSchoolClass().getId() + "_" + a.getSubject().getId(), a -> a));
        List<TeachingAssignment> assignmentsToSave = new ArrayList<>();
        List<TeachingAssignment> assignmentsToDelete = new ArrayList<>();

        for (TeachingAssignmentRequest item : request.assignments()) {
            String key = item.classId() + "_" + item.subjectId();
            TeachingAssignment existing = existingMap.get(key);

            // gỡ phân công
            if (item.teacherId() == null) {
                if (existing != null) {
                    assignmentsToDelete.add(existing);
                }
                continue;
            }
            SchoolClass schoolClass = classMap.get(item.classId());
            Subject subject = subjectMap.get(item.subjectId());
            Teacher teacher = teacherMap.get(item.teacherId());
            if (schoolClass == null || subject == null || teacher == null) {
                throw new ResourceNotFoundException("Dữ liệu Lớp,Môn,GV không tồn tại");
            }
            if (existing != null) {
                // cập nhật
                if (!existing.getTeacher().getId().equals(teacher.getId())) {
                    existing.setTeacher(teacher);
                    assignmentsToSave.add(existing);
                }
            } else {
                // tạo mới
                TeachingAssignment newAssignment = TeachingAssignment.builder()
                        .schoolClass(schoolClass)
                        .subject(subject)
                        .teacher(teacher)
                        .build();
                assignmentsToSave.add(newAssignment);
            }
        }

        if (!assignmentsToSave.isEmpty()) {
            teachingAssignmentRepo.saveAll(assignmentsToSave);
        }
        if (!assignmentsToDelete.isEmpty()) {
            teachingAssignmentRepo.deleteAll(assignmentsToDelete);
        }
    }

    @Override
    public List<TeachingClassResponse> getTeachingClasses(CustomUserPrincipal principal, Long academicYearId) {
        if (!academicYearRepo.existsById(academicYearId))
            throw new ResourceNotFoundException("Năm học không tồn tại");
        List<TeachingAssignment> assignments = teachingAssignmentRepo
                .findByTeacherIdAndSchoolClass_AcademicYear_Id(principal.getId(), academicYearId);

        return assignments.stream().map(assignment -> {
            return TeachingClassResponse.builder()
                    .academicYearId(academicYearId)
                    .schoolClassId(assignment.getSchoolClass().getId())
                    .schoolClassName(assignment.getSchoolClass().getName())
                    .teacherId(principal.getId())
                    .subjectId(assignment.getSubject().getId())
                    .subjectName(assignment.getSubject().getName())
                    .build();
        }).toList();
    }
}