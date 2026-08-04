package com.mth.academicfeesystem.modules.assignment.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.AssignmentStatus;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.assignment.dto.request.HomeroomAssignmentRequest;
import com.mth.academicfeesystem.modules.assignment.dto.response.HomeroomAssignmentResponse;
import com.mth.academicfeesystem.modules.assignment.entity.HomeroomAssignment;
import com.mth.academicfeesystem.modules.assignment.repository.HomeroomAssignmentRepository;
import com.mth.academicfeesystem.modules.assignment.service.HomeroomAssignmentService;
import com.mth.academicfeesystem.modules.people.entity.Teacher;
import com.mth.academicfeesystem.modules.people.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class HomeroomAssignmentServiceImpl implements HomeroomAssignmentService{
    private final SchoolClassRepository schoolClassRepo;
    private final TeacherRepository teacherRepo;
    private final HomeroomAssignmentRepository homeroomAssignmentRepo;

    @Transactional
    @Override
    public HomeroomAssignmentResponse assignmentHomeroomTeacher(HomeroomAssignmentRequest request){
        SchoolClass schoolClass = schoolClassRepo.findById(request.classId())
            .orElseThrow(()->new ResourceNotFoundException("Lớp học không tồn tại"));
        Teacher teacher = teacherRepo.findById(request.teacherId())
            .orElseThrow(()->new ResourceNotFoundException("Giáo viên không tồn tại"));
        Optional<HomeroomAssignment> activeAssignment = homeroomAssignmentRepo
                .findBySchoolClassIdAndStatus(request.classId(), AssignmentStatus.ACTIVE);
        if (activeAssignment.isPresent()) {
            HomeroomAssignment currentAssignment = activeAssignment.get();
            
            if (currentAssignment.getTeacher().getId().equals(request.teacherId())) {
                throw new BusinessException("Giáo viên này hiện đang là chủ nhiệm của lớp " + schoolClass.getName());
            }
            
            currentAssignment.setStatus(AssignmentStatus.ENDED);
            currentAssignment.setEndDate(LocalDate.now());
            homeroomAssignmentRepo.save(currentAssignment);
        }

        HomeroomAssignment newAssignment = HomeroomAssignment.builder()
                .schoolClass(schoolClass)
                .teacher(teacher)
                .startDate(LocalDate.now())
                .status(AssignmentStatus.ACTIVE)
                .build();

        newAssignment = homeroomAssignmentRepo.save(newAssignment);

        return new HomeroomAssignmentResponse(
            newAssignment.getId(),
            schoolClass.getId(),
            schoolClass.getName(),
            teacher.getId(),
            teacher.getUser().getFullName(), 
            newAssignment.getStartDate(),
            newAssignment.getStatus().name()
        );
    }

    @Transactional
    @Override
    public void endHomeroomAssignment(Long homeroomAssignmentId){
        HomeroomAssignment homeroomAssignment = homeroomAssignmentRepo.findById(homeroomAssignmentId)
            .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy giáo phân công chủ nhiệm"));
        if(homeroomAssignment.getStatus()!=AssignmentStatus.ACTIVE)
            throw new BusinessException("Đã kết thúc phân công chủ nhiệm rồi");

        homeroomAssignment.setStatus(AssignmentStatus.ENDED);
        homeroomAssignment.setEndDate(LocalDate.now());

        homeroomAssignmentRepo.save(homeroomAssignment);
    }

    @Transactional
    @Override
    public HomeroomAssignmentResponse getMyHomeroomClass(Long userId) {
        Teacher teacher = teacherRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên"));

        HomeroomAssignment assignment = homeroomAssignmentRepo.findByTeacherIdAndStatus(teacher.getId(), AssignmentStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("Giáo viên này hiện đang không chủ nhiệm lớp nào"));

        return new HomeroomAssignmentResponse(
                assignment.getId(),
                assignment.getSchoolClass().getId(),
                assignment.getSchoolClass().getName(),
                teacher.getId(),
                teacher.getUser().getFullName(),
                assignment.getStartDate(),
                assignment.getStatus().name()
        );
    }
}
