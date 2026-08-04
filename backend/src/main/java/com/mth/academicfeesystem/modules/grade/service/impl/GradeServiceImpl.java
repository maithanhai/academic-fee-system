package com.mth.academicfeesystem.modules.grade.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.modules.academic.entity.Semester;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.repository.SemesterRepository;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.grade.dto.request.GradeRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeDetailResponse;
import com.mth.academicfeesystem.modules.grade.entity.Grade;
import com.mth.academicfeesystem.modules.grade.entity.GradeConfig;
import com.mth.academicfeesystem.modules.grade.repository.GradeConfigRepository;
import com.mth.academicfeesystem.modules.grade.repository.GradeRepository;
import com.mth.academicfeesystem.modules.grade.service.GradeService;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepo;
    private final GradeConfigRepository gradeConfigRepo;
    private final SubjectRepository subjectRepo;
    private final SemesterRepository semesterRepo;
    private final StudentRepository studentRepo;

    @Transactional
    @Override
    public void inputGrades(GradeRequest request) {
        GradeConfig config = gradeConfigRepo.findBySubjectIdAndExamType(request.subjectId(), request.examType())
                .orElseThrow(() -> new BusinessException("Môn học chưa có cấu hình loại điểm này"));

        if (request.ordinalNumber() > config.getMaxColumn()) {
            throw new BusinessException("Cột điểm thứ " + request.ordinalNumber() +
                    " vượt quá số lượng cho phép (" + config.getMaxColumn() + " cột) của cấu hình!");
        }

        Subject subjectRef = subjectRepo.getReferenceById(request.subjectId());
        Semester semesterRef = semesterRepo.getReferenceById(request.semesterId());

        List<Grade> gradesToSave = new ArrayList<>();

        for (var scoreDto : request.scores()) {
            boolean isExist = gradeRepo.existsByStudentIdAndSubjectIdAndSemesterIdAndExamTypeAndOrdinalNumber(
                    scoreDto.studentId(), request.subjectId(), request.semesterId(), request.examType(),
                    request.ordinalNumber());

            if (isExist) {
                throw new BusinessException("Học sinh có ID " + scoreDto.studentId() +
                        " đã có điểm ở cột " + request.ordinalNumber() + " loại " + request.examType());
            }

            Student studentRef = studentRepo.getReferenceById(scoreDto.studentId());
            Grade grade = Grade.builder()
                    .student(studentRef)
                    .subject(subjectRef)
                    .semester(semesterRef)
                    .examType(request.examType())
                    .ordinalNumber(request.ordinalNumber())
                    .scoreValue(scoreDto.scoreValue())
                    .build();

            gradesToSave.add(grade);
        }
        gradeRepo.saveAll(gradesToSave);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GradeDetailResponse> getSubjectGradeBoard(Long classId, Long subjectId, Long semesterId) {
        List<Grade> grades = gradeRepo.findSubjectTeacherGrades(classId, subjectId, semesterId);
        return grades.stream().map(g -> new GradeDetailResponse(
                g.getId(),
                g.getStudent().getId(),
                g.getStudent().getUser().getFullName(),
                g.getSubject().getName(),
                g.getExamType(),
                g.getOrdinalNumber(),
                g.getScoreValue())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GradeDetailResponse> getMyGrades(Long studentId, Long semesterId) {

        List<Grade> grades = gradeRepo.findGradesByStudent(studentId, semesterId);

        return grades.stream().map(g -> new GradeDetailResponse(
                g.getId(),
                g.getStudent().getId(),
                g.getStudent().getUser().getFullName(),
                g.getSubject().getName(),
                g.getExamType(),
                g.getOrdinalNumber(),
                g.getScoreValue())).collect(Collectors.toList());
    }
}
