package com.mth.academicfeesystem.modules.assignment.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassListResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkHomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.request.BulkHomeroomAssignmentRequest.HomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.entity.HomeroomAssignment;
import com.mth.academicfeesystem.modules.assignment.mapper.HomeroomAssignmentMapper;
import com.mth.academicfeesystem.modules.assignment.repository.HomeroomAssignmentRepository;
import com.mth.academicfeesystem.modules.assignment.service.HomeroomAssignmentService;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeroomAssignmentServiceImpl implements HomeroomAssignmentService {
        private final SchoolClassRepository schoolClassRepo;
        private final TeacherRepository teacherRepo;
        private final HomeroomAssignmentRepository homeroomAssignmentRepo;
        private final AcademicYearRepository academicYearRepo;
        private final HomeroomAssignmentMapper homeroomAssignmentMapper;

        @Transactional
        @Override
        public void bulkSaveHomeroomAssignments(BulkHomeroomAssignmentRequest request) {
                Long academicYearId = request.academicYearId();
                List<HomeroomAssignmentRequest> assignments = request.assignments();
                if (assignments == null || assignments.isEmpty())
                        return;

                AcademicYear academicYear = academicYearRepo.findById(academicYearId)
                                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
                if (!academicYear.getActive()) {
                        throw new BusinessException("Năm học đã khóa sổ, không thể thay đổi phân công");
                }
                // gom id để truy vấn
                List<Long> classIds = assignments.stream().map(HomeroomAssignmentRequest::classId).distinct().toList();
                List<Long> teacherIds = assignments.stream().map(HomeroomAssignmentRequest::teacherId)
                                .filter(id -> id != null).distinct().toList();

                Map<Long, SchoolClass> classMap = schoolClassRepo.findAllById(classIds).stream()
                                .collect(Collectors.toMap(SchoolClass::getId, c -> c));
                Map<Long, Teacher> teacherMap = teacherRepo.findAllById(teacherIds).stream()
                                .collect(Collectors.toMap(Teacher::getId, t -> t));
                Map<Long, HomeroomAssignment> classActiveAssignments = homeroomAssignmentRepo
                                .findBySchoolClassIdInAndStatus(classIds, AssignmentStatus.ACTIVE).stream()
                                .collect(Collectors.toMap(a -> a.getSchoolClass().getId(), a -> a));
                Map<Long, HomeroomAssignment> teacherActiveAssignments = homeroomAssignmentRepo
                                .findByTeacherIdInAndStatus(teacherIds, AssignmentStatus.ACTIVE).stream()
                                .collect(Collectors.toMap(a -> a.getTeacher().getId(), a -> a));

                List<HomeroomAssignment> assignmentsToSave = new ArrayList<>();

                for (HomeroomAssignmentRequest item : assignments) {
                        SchoolClass schoolClass = classMap.get(item.classId());
                        if (schoolClass == null)
                                throw new ResourceNotFoundException("Lớp học không tồn tại");
                        if (!schoolClass.getAcademicYear().getId().equals(academicYear.getId())) {
                                throw new BusinessException("Lớp không thuộc năm học hiện tại!");
                        }
                        HomeroomAssignment activeAssignment = classActiveAssignments.get(item.classId());

                        if (item.teacherId() == null) {
                                if (activeAssignment != null) {
                                        activeAssignment.setStatus(AssignmentStatus.ENDED);
                                        activeAssignment.setEndDate(LocalDate.now());
                                        assignmentsToSave.add(activeAssignment);
                                }
                                continue;
                        }
                        Teacher teacher = teacherMap.get(item.teacherId());
                        if (teacher == null)
                                throw new ResourceNotFoundException("Giáo viên không tồn tại");

                        HomeroomAssignment teacherCurrentClass = teacherActiveAssignments.get(item.teacherId());
                        if (teacherCurrentClass != null
                                        && !teacherCurrentClass.getSchoolClass().getId().equals(item.classId())) {
                                throw new BusinessException("Giáo viên " + teacher.getUser().getFullName()
                                                + " hiện đang làm chủ nhiệm lớp "
                                                + teacherCurrentClass.getSchoolClass().getName());
                        }

                        if (activeAssignment != null) {
                                if (activeAssignment.getTeacher().getId().equals(teacher.getId()))
                                        continue;
                                activeAssignment.setStatus(AssignmentStatus.ENDED);
                                activeAssignment.setEndDate(LocalDate.now());
                                assignmentsToSave.add(activeAssignment);
                        }

                        HomeroomAssignment newAssignment = HomeroomAssignment.builder()
                                        .schoolClass(schoolClass)
                                        .teacher(teacher)
                                        .startDate(LocalDate.now())
                                        .status(AssignmentStatus.ACTIVE)
                                        .build();
                        assignmentsToSave.add(newAssignment);
                }
                if (!assignmentsToSave.isEmpty()) {
                        homeroomAssignmentRepo.saveAll(assignmentsToSave); // <--- BULK INSERT/UPDATE
                }
        }

        @Transactional
        @Override
        public void endHomeroomAssignment(Long homeroomAssignmentId) {
                HomeroomAssignment homeroomAssignment = homeroomAssignmentRepo.findById(homeroomAssignmentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Không tìm thấy giáo phân công chủ nhiệm"));
                if (homeroomAssignment.getStatus() != AssignmentStatus.ACTIVE)
                        throw new BusinessException("Đã kết thúc phân công chủ nhiệm rồi");

                homeroomAssignment.setStatus(AssignmentStatus.ENDED);
                homeroomAssignment.setEndDate(LocalDate.now());

                homeroomAssignmentRepo.save(homeroomAssignment);
        }

        // @Transactional
        // @Override
        // public HomeroomAssignmentResponse getMyHomeroomClass(Long userId) {
        // Teacher teacher = teacherRepo.findById(userId)
        // .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo
        // viên"));

        // HomeroomAssignment assignment = homeroomAssignmentRepo
        // .findByTeacherIdAndStatus(teacher.getId(), AssignmentStatus.ACTIVE)
        // .orElseThrow(() -> new BusinessException(
        // "Giáo viên này hiện đang không chủ nhiệm lớp nào"));

        // return HomeroomAssignmentResponse.builder()
        // .id(assignment.getId())
        // .classId(assignment.getSchoolClass().getId())
        // .className(assignment.getSchoolClass().getName())
        // .teacherId(teacher.getId())
        // .teacherName(teacher.getUser().getFullName())
        // .startDate(assignment.getStartDate())
        // .status(assignment.getStatus().name())
        // .build();
        // }

        @Override
        public List<HomeroomAssignmentResponse> getExistingAssignments(Long academicYearId) {
                List<Long> classIds = schoolClassRepo.findByAcademicYearId(academicYearId)
                                .stream().map(SchoolClass::getId).toList();
                if (classIds.isEmpty())
                        return new ArrayList<>();

                return homeroomAssignmentRepo.findBySchoolClassIdInAndStatus(classIds, AssignmentStatus.ACTIVE)
                                .stream()
                                .map(assignment -> homeroomAssignmentMapper.toResponse(assignment))
                                .toList();
        }

        @Override
        public List<SchoolClassListResponse> getHomeroomeClass(CustomUserPrincipal principal) {
                List<HomeroomAssignment> homeroomAssignments = homeroomAssignmentRepo
                                .findAllByTeacherId(principal.getId());
                if (homeroomAssignments.isEmpty())
                        return Collections.emptyList();
                List<Long> classIds = homeroomAssignments.stream()
                                .map(homeroomAssignment -> homeroomAssignment.getSchoolClass().getId()).distinct()
                                .toList();
                List<Object[]> rawResults = schoolClassRepo.countStudentsByClassIds(classIds);
                Map<Long, Long> countStudentsMap = new HashMap<>();
                for (Object[] row : rawResults) {
                        Long classId = (Long) row[0];
                        Long count = (Long) row[1];
                        countStudentsMap.put(classId, count);
                }
                return homeroomAssignments.stream().map(assignment -> {
                        SchoolClass schoolClass = assignment.getSchoolClass();
                        Long schoolClassId = schoolClass.getId();
                        return SchoolClassListResponse.builder()
                                        .id(schoolClassId)
                                        .name(schoolClass.getName())
                                        .gradeLevel(schoolClass.getGradeLevel())
                                        .academicYearId(schoolClass.getAcademicYear().getId())
                                        .totalStudents(countStudentsMap.get(schoolClassId) == null ? 0
                                                        : countStudentsMap.get(schoolClassId))
                                        .build();
                }).toList();
        }
}
