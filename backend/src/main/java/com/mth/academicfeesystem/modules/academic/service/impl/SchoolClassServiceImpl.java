package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassListResponse;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.mapper.SchoolClassMapper;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.ClassEnrollmentRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.service.SchoolClassService;
import com.mth.academicfeesystem.modules.people.dto.response.StudentShortListResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {
        private final SchoolClassRepository schoolClassRepo;
        private final SchoolClassMapper schoolClassMapper;
        private final AcademicYearRepository academicYearRepo;
        private final ClassEnrollmentRepository classEnrollmentRepo;
        // Nằm trong SchoolClassService.java

        @Override
        public List<SchoolClassResponse> getClasses() {
                List<SchoolClass> classes = schoolClassRepo.findAll();
                return schoolClassMapper.toListResponse(classes);
        }

        @Transactional
        @Override
        public SchoolClassResponse createClass(SchoolClassRequest request) {
                SchoolClass schoolClass = new SchoolClass();
                schoolClass.setName(request.className());
                schoolClass.setGradeLevel(request.gradeLevel());
                AcademicYear academicYear = academicYearRepo.findById(request.academicYearId())
                                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
                schoolClass.setAcademicYear(academicYear);
                SchoolClass savedClass = schoolClassRepo.save(schoolClass);
                return schoolClassMapper.toResponse(savedClass);
        }

        @Transactional()
        public void autoPromoteStudents() {
                int currentYear = LocalDate.now().getYear();
                String newAcademicYear = currentYear + "-" + (currentYear + 1);
                String oldAcademicYear = (currentYear - 1) + "-" + currentYear;

                AcademicYear oldYear = academicYearRepo.findByName(oldAcademicYear)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Old academic year not found: " + oldAcademicYear));

                AcademicYear newYear = academicYearRepo.findByName(newAcademicYear)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "New academic year not exists: " + newAcademicYear));

                if (schoolClassRepo.existsByAcademicYearIdAndGradeLevelIn(newYear.getId(), Arrays.asList(11, 12))) {
                        throw new DuplicateResourceException(
                                        "Hệ thống đã thực hiện lên lớp cho năm học " + newAcademicYear + " rồi!");
                }

                List<SchoolClass> oldClasses = schoolClassRepo.findByAcademicYearIdAndGradeLevelIn(oldYear.getId(),
                                Arrays.asList(10, 11));
                if (oldClasses.isEmpty()) {
                        throw new ResourceNotFoundException(
                                        "Không có lớp Khối 10 hoặc 11 trong năm " + oldAcademicYear + " để lên lớp.");
                }

                List<Long> oldClassIds = oldClasses.stream()
                                .map(schoolClass -> schoolClass.getId())
                                .collect(Collectors.toList());

                List<ClassEnrollment> allActiveStudents = classEnrollmentRepo.findBySchoolClassIdInAndStatus(
                                oldClassIds,
                                EnrollmentStatus.ACTIVE);

                Map<Long, List<ClassEnrollment>> studentsByOldClass = allActiveStudents.stream()
                                .collect(Collectors.groupingBy(enrollment -> enrollment.getSchoolClass().getId()));
                List<ClassEnrollment> allEnrollmentsToSave = new ArrayList<>();

                for (SchoolClass oldClass : oldClasses) {

                        Integer newGrade = oldClass.getGradeLevel() + 1;
                        String newName = oldClass.getName().replaceFirst(
                                        String.valueOf(oldClass.getGradeLevel()),
                                        String.valueOf(newGrade));

                        SchoolClass newClass = SchoolClass.builder()
                                        .name(newName)
                                        .gradeLevel(newGrade)
                                        .academicYear(newYear)
                                        .build();

                        newClass = schoolClassRepo.save(newClass);

                        List<ClassEnrollment> activeStudentsInClass = studentsByOldClass.getOrDefault(oldClass.getId(),
                                        Collections.emptyList());

                        for (ClassEnrollment oldEnrollment : activeStudentsInClass) {
                                oldEnrollment.setStatus(EnrollmentStatus.TRANSFERRED);
                                oldEnrollment.setEndDate(LocalDate.now());
                                allEnrollmentsToSave.add(oldEnrollment);

                                ClassEnrollment newEnrollment = ClassEnrollment.builder()
                                                .student(oldEnrollment.getStudent())
                                                .schoolClass(newClass)
                                                .status(EnrollmentStatus.ACTIVE)
                                                .startDate(LocalDate.now())
                                                .build();
                                allEnrollmentsToSave.add(newEnrollment);
                        }
                }
                classEnrollmentRepo.saveAll(allEnrollmentsToSave);
        }

        @Override
        public List<SchoolClassListResponse> getClassesByAcademicYearId(Long academicYearId) {
                List<SchoolClass> schoolClasses = schoolClassRepo.findByAcademicYearId(academicYearId);

                List<Long> classIds = schoolClasses.stream().map(SchoolClass::getId).distinct().toList();
                List<Object[]> countResults = schoolClassRepo.countStudentsByClassIds(classIds);
                Map<Long, Long> countStudentsMap = new HashMap<>();
                for (Object[] row : countResults) {
                        Long classId = (Long) row[0];
                        Long count = (Long) row[1];
                        countStudentsMap.put(classId, count);
                }
                return schoolClasses.stream().map(response -> {
                        Long schoolClassId = response.getId();
                        return SchoolClassListResponse.builder()
                                        .id(schoolClassId)
                                        .name(response.getName())
                                        .gradeLevel(response.getGradeLevel())
                                        .academicYearId(academicYearId)
                                        .totalStudents(countStudentsMap.get(schoolClassId) == null ? 0
                                                        : countStudentsMap.get(schoolClassId))
                                        .build();
                }).toList();
        }

        @Override
        public List<StudentShortListResponse> getStudentsByClassId(Long classId) {
                SchoolClass schoolClass = schoolClassRepo.findById(classId)
                                .orElseThrow(() -> new ResourceNotFoundException("Lớp học không tồn tại"));
                List<ClassEnrollment> classEnrollments;
                if (Boolean.TRUE.equals(schoolClass.getAcademicYear().getActive())) {
                        classEnrollments = classEnrollmentRepo.findBySchoolClassIdAndStatus(classId,
                                        EnrollmentStatus.ACTIVE);
                } else {
                        classEnrollments = classEnrollmentRepo.findBySchoolClassId(classId);
                }

                return classEnrollments.stream().map(enrollment -> StudentShortListResponse.builder()
                                .enrollmentId(enrollment.getId())
                                .studentId(enrollment.getStudent().getId())
                                .studentName(enrollment.getStudent().getUser().getFullName())
                                .dayOfBirth(enrollment.getStudent().getUser().getDateOfBirth())
                                .gender(enrollment.getStudent().getUser().getGender())
                                .build()).toList();
        }
}
