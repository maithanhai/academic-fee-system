package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.request.TransferStudentRequest;
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
    public void transferStudent(TransferStudentRequest request) {
        SchoolClass targetClass = schoolClassRepo.findById(request.targetClassId())
                .orElseThrow(() -> new ResourceNotFoundException("School class not found"));

        Student student = studentRepo.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        ClassEnrollment currentEnrollment = classEnrollmentRepo
                .findByStudentIdAndStatus(request.studentId(), EnrollmentStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("Student is not currently enrolled or inactive"));

        if (currentEnrollment.getSchoolClass().getId().equals(request.targetClassId())) {
            throw new BusinessException("Student is already in this class");
        }

        if (!currentEnrollment.getSchoolClass().getAcademicYear().getId()
                .equals(targetClass.getAcademicYear().getId())) {
            throw new BusinessException("Cannot transfer student between different academic years");
        }
        currentEnrollment.setStatus(EnrollmentStatus.TRANSFERRED);
        currentEnrollment.setEndDate(LocalDate.now());
        classEnrollmentRepo.save(currentEnrollment);
        ClassEnrollment newEnrollment = ClassEnrollment.builder()
                .student(student)
                .schoolClass(targetClass)
                .status(EnrollmentStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();
        classEnrollmentRepo.save(newEnrollment);
    }
}
