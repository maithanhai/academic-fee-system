package com.mth.academicfeesystem.modules.grade.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.ExamType;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.entity.Semester;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.repository.ClassEnrollmentRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.repository.SemesterRepository;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.audit.annotation.Auditable;
import com.mth.academicfeesystem.modules.audit.aspect.AuditContext;
import com.mth.academicfeesystem.modules.grade.dto.request.GradeSaveRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse.GradeColumnConfig;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse.StudentGradeRow;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeTableResponse.StudentGradeRow.GradeDetailResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse.ExamGroup;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse.GradeDetail;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse.SemesterTranscript;
import com.mth.academicfeesystem.modules.grade.dto.response.StudentTranscriptResponse.SubjectScore;
import com.mth.academicfeesystem.modules.grade.entity.Grade;
import com.mth.academicfeesystem.modules.grade.entity.GradeConfig;
import com.mth.academicfeesystem.modules.grade.repository.GradeConfigRepository;
import com.mth.academicfeesystem.modules.grade.repository.GradeRepository;
import com.mth.academicfeesystem.modules.grade.service.GradeService;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
        private final GradeRepository gradeRepo;
        private final GradeConfigRepository gradeConfigRepo;
        private final SubjectRepository subjectRepo;
        private final SemesterRepository semesterRepo;
        private final StudentRepository studentRepo;
        private final ClassEnrollmentRepository classEnrollmentRepo;
        private final SchoolClassRepository schoolClassRepo;

        @Override
        public StudentTranscriptResponse getTranscriptByAdmin(Long studentId, Long classId) {
                Student student = studentRepo.findById(studentId)
                                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh"));
                SchoolClass schoolClass = schoolClassRepo.findById(classId)
                                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học"));

                Long academicYearId = schoolClass.getAcademicYear().getId();
                List<Grade> grades = gradeRepo.findByStudentIdAndAcademicYearId(studentId, academicYearId);
                Map<Semester, List<Grade>> gradesBySemester = grades.stream()
                                .collect(Collectors.groupingBy(Grade::getSemester));
                List<SemesterTranscript> semesterTranscripts = gradesBySemester.entrySet().stream()
                                .map(this::buildSemesterTranscript)
                                .toList();
                return StudentTranscriptResponse.builder()
                                .studentId(student.getId())
                                .fullName(student.getUser().getFullName())
                                .semesters(semesterTranscripts)
                                .build();
        }

        private SemesterTranscript buildSemesterTranscript(Map.Entry<Semester, List<Grade>> semesterEntry) {
                Semester semester = semesterEntry.getKey();
                List<Grade> gradesInSemester = semesterEntry.getValue();
                // gom theo môn học
                Map<Subject, List<Grade>> gradesBySubject = gradesInSemester.stream()
                                .collect(Collectors.groupingBy(Grade::getSubject));

                List<SubjectScore> subjectScores = gradesBySubject.entrySet().stream()
                                .map(this::buildSubjectScore)
                                .toList();

                return SemesterTranscript.builder()
                                .semesterId(semester.getId())
                                .semesterName(semester.getName().name())
                                .subjectScores(subjectScores)
                                .build();
        }

        private SubjectScore buildSubjectScore(Map.Entry<Subject, List<Grade>> subjectEntry) {
                Subject subject = subjectEntry.getKey();
                List<Grade> gradesInSubject = subjectEntry.getValue();
                // gom theo loại điểm
                Map<ExamType, List<Grade>> gradesByExamType = gradesInSubject.stream()
                                .collect(Collectors.groupingBy(Grade::getExamType));
                List<ExamGroup> examGroups = gradesByExamType.entrySet().stream()
                                .map(examTypeEntry -> {
                                        ExamType examType = examTypeEntry.getKey();
                                        List<GradeDetail> scoreDetails = examTypeEntry.getValue().stream()
                                                        .sorted(Comparator.comparingInt(Grade::getOrdinalNumber))
                                                        .map(g -> GradeDetail.builder()
                                                                        .gradeId(g.getId())
                                                                        .scoreValue(g.getScoreValue())
                                                                        .build())
                                                        .toList();
                                        return ExamGroup.builder()
                                                        .examType(examType)
                                                        .scores(scoreDetails)
                                                        .build();
                                })
                                .toList();
                return SubjectScore.builder()
                                .subjectId(subject.getId())
                                .subjectName(subject.getName())
                                .examGroups(examGroups)
                                .build();
        }

        @Override
        public StudentTranscriptResponse getTranscriptByTeacher(Long studentId, Long schoolClassId) {
                Student student = studentRepo.findById(studentId)
                                .orElseThrow(() -> new ResourceNotFoundException("Học sinh không tồn tại"));
                if (!schoolClassRepo.existsById(schoolClassId))
                        throw new ResourceNotFoundException("Lớp học không tồn tại");
                if (!classEnrollmentRepo.existsBySchoolClassIdAndStudentId(schoolClassId, studentId))
                        throw new ResourceNotFoundException("Học sinh không thuộc lớp học này");
                List<Grade> grades = gradeRepo.findByStudentId(studentId);
                Map<Semester, List<Grade>> gradesBySemester = grades.stream()
                                .collect(Collectors.groupingBy(Grade::getSemester));

                List<SemesterTranscript> semesterTranscripts = gradesBySemester.entrySet().stream()
                                .map(this::buildSemesterTranscript)
                                .toList();
                return StudentTranscriptResponse.builder()
                                .studentId(student.getId())
                                .fullName(student.getUser().getFullName())
                                .semesters(semesterTranscripts)
                                .build();
        }

        @Transactional(readOnly = true)
        @Override
        public GradeTableResponse getGradeTable(Long classId, Long subjectId, Long semesterId) {
                SchoolClass schoolClass = schoolClassRepo.findById(classId)
                                .orElseThrow(() -> new ResourceNotFoundException("Lớp học không tồn tại"));
                List<GradeConfig> configs = gradeConfigRepo.findBySubjectId(subjectId);
                List<GradeColumnConfig> columns = configs.stream()
                                .map(c -> GradeColumnConfig.builder()
                                                .examType(c.getExamType().name())
                                                .coefficient(c.getCoefficient())
                                                .maxColumn(c.getMaxColumn())
                                                .build())
                                .toList();

                List<Student> students;
                if (schoolClass.getAcademicYear().getActive()) {
                        students = classEnrollmentRepo.findActiveStudentsByClassId(classId);
                } else {
                        students = classEnrollmentRepo.findHistoricalStudentsByClassId(classId);
                }
                if (students.isEmpty()) {
                        return GradeTableResponse.builder()
                                        .classId(classId)
                                        .subjectId(subjectId)
                                        .semesterId(semesterId)
                                        .columns(columns)
                                        .rows(Collections.emptyList())
                                        .build();
                }
                List<Long> studentIds = students.stream().map(Student::getId).toList();
                List<Grade> grades = gradeRepo.findBySubjectIdAndSemesterIdAndStudentIdIn(subjectId, semesterId,
                                studentIds);

                Map<Long, Map<String, List<GradeDetailResponse>>> gradeMap = grades.stream()
                                .collect(Collectors.groupingBy(
                                                g -> g.getStudent().getId(),
                                                Collectors.groupingBy(
                                                                g -> g.getExamType().name(),
                                                                Collectors.mapping(
                                                                                g -> GradeDetailResponse.builder()
                                                                                                .gradeId(g.getId())
                                                                                                .score(g.getScoreValue())
                                                                                                .ordinalNumber(g.getOrdinalNumber())
                                                                                                .build(),
                                                                                Collectors.toList()))));
                List<StudentGradeRow> rows = students.stream()
                                .map(st -> {
                                        Map<String, List<GradeDetailResponse>> stGrades = gradeMap
                                                        .getOrDefault(st.getId(), new HashMap<>());

                                        return StudentGradeRow.builder()
                                                        .studentId(st.getId())
                                                        .studentName(st.getUser().getFullName())
                                                        .gender(st.getUser().getGender().name())
                                                        .scores(stGrades)
                                                        .build();
                                })
                                .sorted(Comparator.comparing(StudentGradeRow::studentName))
                                .toList();

                return GradeTableResponse.builder()
                                .classId(classId)
                                .subjectId(subjectId)
                                .semesterId(semesterId)
                                .columns(columns)
                                .rows(rows)
                                .build();
        }

        @Transactional
        @Override
        @Auditable(action = "UPDATE_GRADE", targetTable = "grades")
        public GradeDetailResponse autoSaveGrade(GradeSaveRequest request, Long subjectId) {
                GradeConfig config = gradeConfigRepo.findBySubjectIdAndExamType(subjectId, request.examType())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Cấu hình điểm cho cho cột này không tồn tại!"));
                if (request.ordinalNumber() < 1 || request.ordinalNumber() > config.getMaxColumn()) {
                        throw new BusinessException("Thứ tự cột điểm không hợp lệ");
                }
                Optional<Grade> existingGrade = gradeRepo
                                .findByStudentIdAndSubjectIdAndSemesterIdAndExamTypeAndOrdinalNumber(
                                                request.studentId(),
                                                subjectId,
                                                request.semesterId(),
                                                request.examType(),
                                                request.ordinalNumber());

                Grade grade;
                if (existingGrade.isPresent()) {
                        grade = existingGrade.get();
                        if (grade.getScoreValue() == request.score()) {
                                AuditContext.setBefore(null);
                                return GradeDetailResponse.builder()
                                                .gradeId(grade.getId())
                                                .score(grade.getScoreValue())
                                                .ordinalNumber(grade.getOrdinalNumber())
                                                .build();
                        }
                        AuditContext.setBefore(GradeDetailResponse.builder()
                                        .gradeId(grade.getId())
                                        .score(grade.getScoreValue())
                                        .ordinalNumber(grade.getOrdinalNumber())
                                        .build());
                        grade.setScoreValue(request.score());
                } else {
                        Student student = studentRepo.getReferenceById(request.studentId());
                        Subject subject = subjectRepo.getReferenceById(subjectId);
                        Semester semester = semesterRepo.getReferenceById(request.semesterId());

                        grade = Grade.builder()
                                        .student(student)
                                        .subject(subject)
                                        .semester(semester)
                                        .examType(request.examType())
                                        .ordinalNumber(request.ordinalNumber())
                                        .scoreValue(request.score())
                                        .build();
                        AuditContext.setBefore(null);
                }
                Grade savedGrade = gradeRepo.save(grade);
                return GradeDetailResponse.builder()
                                .gradeId(savedGrade.getId())
                                .score(savedGrade.getScoreValue())
                                .ordinalNumber(savedGrade.getOrdinalNumber())
                                .build();
        }

        @Override
        public StudentTranscriptResponse getTranscriptByStudent(CustomUserPrincipal principal, Long academicYearId) {
                List<Grade> grades = gradeRepo.findByStudentIdAndAcademicYearId(principal.getId(), academicYearId);
                Map<Semester, List<Grade>> gradesBySemester = grades.stream()
                                .collect(Collectors.groupingBy(Grade::getSemester));
                List<SemesterTranscript> semesterTranscripts = gradesBySemester.entrySet().stream()
                                .map(this::buildSemesterTranscript)
                                .toList();
                return StudentTranscriptResponse.builder()
                                .studentId(principal.getId())
                                .fullName(principal.getUser().getFullName())
                                .semesters(semesterTranscripts)
                                .build();
        }
}
