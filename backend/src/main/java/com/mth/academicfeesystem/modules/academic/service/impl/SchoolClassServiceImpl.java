package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.exception.BusinessException;
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

        @Transactional
        @Override
        public void autoPromoteStudentsByClassId(Long classId) {
                SchoolClass oldClass = schoolClassRepo.findById(classId)
                                .orElseThrow(() -> new ResourceNotFoundException("Lớp học không tồn tại"));
                AcademicYear oldYear = oldClass.getAcademicYear();

                if (Boolean.TRUE.equals(oldYear.getActive())) {
                        throw new BusinessException("Chỉ được lên lớp sau khi năm học đã kết thúc");
                }
                if (oldClass.getGradeLevel() < 10 || oldClass.getGradeLevel() >= 12) {
                        throw new BusinessException("Chỉ được lên lớp cho lớp khối 10 hoặc 11");
                }

                String[] yearParts = oldYear.getName().split("\\s*-\\s*");
                int oldEndYear = Integer.parseInt(yearParts[1].trim());
                String newYearName = oldEndYear + "-" + (oldEndYear + 1);
                AcademicYear newYear = academicYearRepo.findByName(newYearName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Năm học kế tiếp không tồn tại"));
                if (!Boolean.TRUE.equals(newYear.getActive())) {
                        throw new BusinessException("Năm học đích đã đóng, không thể lên lớp");
                }
                // kiểm tra khối lớp và tên lớp
                Integer newGradeLevel = oldClass.getGradeLevel() + 1;
                String newClassName = oldClass.getName().replaceFirst(
                                "^" + oldClass.getGradeLevel(), String.valueOf(newGradeLevel));
                if (newClassName.equals(oldClass.getName())) {
                        throw new BusinessException("Không xác định được tên lớp đích từ lớp " + oldClass.getName());
                }
                // kiểm tra lớp mới
                SchoolClass newClass = schoolClassRepo.findByAcademicYearIdAndName(newYear.getId(), newClassName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Chưa tạo lớp đích " + newClassName + " trong năm học " + newYearName));
                if (!newGradeLevel.equals(newClass.getGradeLevel())) {
                        throw new BusinessException("Lớp đích không thuộc khối kế tiếp của lớp nguồn");
                }
                List<ClassEnrollment> oldEnrollments = classEnrollmentRepo.findBySchoolClassIdAndStatus(
                                oldClass.getId(), EnrollmentStatus.DROPPED);
                if (oldEnrollments.isEmpty()) {
                        throw new ResourceNotFoundException("Lớp không có học sinh đã kết thúc để lên lớp");
                }

                Set<Long> targetStudentIds = new HashSet<>(classEnrollmentRepo
                                .findBySchoolClassIdAndStatus(newClass.getId(), EnrollmentStatus.ACTIVE).stream()
                                .map(enrollment -> enrollment.getStudent().getId())
                                .toList());
                boolean alreadyPromoted = oldEnrollments.stream()
                                .anyMatch(enrollment -> targetStudentIds.contains(enrollment.getStudent().getId()));
                if (alreadyPromoted) {
                        throw new BusinessException("Một hoặc nhiều học sinh của lớp đã được lên lớp trước đó");
                }

                List<ClassEnrollment> newEnrollments = oldEnrollments.stream()
                                .map(oldEnrollment -> ClassEnrollment.builder()
                                                .student(oldEnrollment.getStudent())
                                                .schoolClass(newClass)
                                                .status(EnrollmentStatus.ACTIVE)
                                                .startDate(LocalDate.now())
                                                .build())
                                .toList();
                classEnrollmentRepo.saveAll(newEnrollments);
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
                if (!schoolClassRepo.existsById(classId))
                        throw new ResourceNotFoundException("Lớp học không tồn tại");
                List<ClassEnrollment> classEnrollments = classEnrollmentRepo.findBySchoolClassIdAndStatus(classId,
                                EnrollmentStatus.ACTIVE);
                return classEnrollments.stream().map(enrollment -> StudentShortListResponse.builder()
                                .enrollmentId(enrollment.getId())
                                .studentId(enrollment.getStudent().getId())
                                .studentName(enrollment.getStudent().getUser().getFullName())
                                .dayOfBirth(enrollment.getStudent().getUser().getDateOfBirth())
                                .gender(enrollment.getStudent().getUser().getGender())
                                .build()).toList();
        }
}
