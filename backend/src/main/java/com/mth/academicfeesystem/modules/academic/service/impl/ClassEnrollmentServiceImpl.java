package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.request.EnrollmentRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.TransferRequest;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.repository.ClassEnrollmentRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.service.ClassEnrollmentService;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {
        private final SchoolClassRepository schoolClassRepo;
        private final StudentRepository studentRepo;
        private final ClassEnrollmentRepository classEnrollmentRepo;

        @Transactional
        @Override
        public void enrollStudents(EnrollmentRequest request) {
                if (!schoolClassRepo.existsById(request.classId()))
                        throw new ResourceNotFoundException("Lớp học không tồn tại");
                SchoolClass schoolClass = schoolClassRepo.getReferenceById(request.classId());
                List<ClassEnrollment> classEnrollments = request.studentIds().stream().map(studentId -> {
                        Student student = studentRepo.getReferenceById(studentId);
                        return ClassEnrollment.builder().schoolClass(schoolClass).student(student)
                                        .status(EnrollmentStatus.ACTIVE).build();
                }).toList();
                classEnrollmentRepo.saveAll(classEnrollments);
        }

        @Transactional
        @Override
        public void transferStudent(TransferRequest request) {
                ClassEnrollment oldEnrollment = classEnrollmentRepo.findById(request.enrollmentId())
                                .orElseThrow(() -> new ResourceNotFoundException("Bản ghi đăng ký lớp không tồn tại"));
                SchoolClass newSchoolClass = schoolClassRepo.findById(request.newClassId())
                                .orElseThrow(() -> new ResourceNotFoundException("Lớp học không tồn tại"));
                oldEnrollment.setStatus(EnrollmentStatus.TRANSFERRED);
                oldEnrollment.setEndDate(LocalDate.now());
                ClassEnrollment newEnrollment = ClassEnrollment.builder().schoolClass(newSchoolClass)
                                .student(oldEnrollment.getStudent()).status(EnrollmentStatus.ACTIVE).build();
                classEnrollmentRepo.saveAll(List.of(newEnrollment, oldEnrollment));
        }

        @Transactional 
        public void dropEnrollmentsByAcademicYearId(Long academicYearId){
                List<ClassEnrollment> enrollmentsToDrop = classEnrollmentRepo.findBySchoolClassAcademicYearId(academicYearId);
                for (ClassEnrollment enrollment : enrollmentsToDrop) {
                        if (enrollment.getStatus() == EnrollmentStatus.ACTIVE) {
                                enrollment.setStatus(EnrollmentStatus.DROPPED);
                                enrollment.setEndDate(LocalDate.now());
                        }
                }
                classEnrollmentRepo.saveAll(enrollmentsToDrop);
        }
}
