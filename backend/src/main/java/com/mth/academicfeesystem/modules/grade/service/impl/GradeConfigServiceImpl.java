package com.mth.academicfeesystem.modules.grade.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.enums.ExamType;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.grade.dto.request.GradeConfigsRequest;
import com.mth.academicfeesystem.modules.grade.dto.request.UpdateGradeConfigsRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeConfigsResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeConfigsResponse.ConfigDetailResponse;
import com.mth.academicfeesystem.modules.grade.entity.GradeConfig;
import com.mth.academicfeesystem.modules.grade.repository.GradeConfigRepository;
import com.mth.academicfeesystem.modules.grade.service.GradeConfigService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeConfigServiceImpl implements GradeConfigService {
        private final GradeConfigRepository gradeConfigRepo;
        private final SubjectRepository subjectRepo;

        // Tao config
        @Transactional
        @Override
        public GradeConfigsResponse createGradeConfigs(GradeConfigsRequest request) {
                Subject subject = subjectRepo.findById(request.subjectId())
                                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));

                GradeConfig oral = GradeConfig.builder()
                                .subject(subject)
                                .examType(ExamType.MIENG)
                                .coefficient(request.oralExamConfig().coefficient())
                                .maxColumn(request.oralExamConfig().maxColumn())
                                .build();
                GradeConfig quiz = GradeConfig.builder()
                                .subject(subject)
                                .examType(ExamType.PHUT_15)
                                .coefficient(request.quizExamConfig().coefficient())
                                .maxColumn(request.quizExamConfig().maxColumn())
                                .build();
                GradeConfig midterm = GradeConfig.builder()
                                .subject(subject)
                                .examType(ExamType.TIET_1)
                                .coefficient(request.midtermExamConfig().coefficient())
                                .maxColumn(request.midtermExamConfig().maxColumn())
                                .build();
                GradeConfig finalExam = GradeConfig.builder()
                                .subject(subject)
                                .examType(ExamType.HOC_KY)
                                .coefficient(request.finalExamConfig().coefficient())
                                .maxColumn(request.finalExamConfig().maxColumn())
                                .build();

                gradeConfigRepo.saveAll(List.of(oral, quiz, midterm, finalExam));
                return GradeConfigsResponse.builder()
                                .subjectId(subject.getId())
                                .subjectName(subject.getName())
                                .oralExamConfig(ConfigDetailResponse.builder()
                                                .coefficient(oral.getCoefficient())
                                                .maxColumn(oral.getMaxColumn())
                                                .build())
                                .quizExamConfig(ConfigDetailResponse.builder()
                                                .coefficient(quiz.getCoefficient())
                                                .maxColumn(quiz.getMaxColumn())
                                                .build())
                                .midtermExamConfig(ConfigDetailResponse.builder()
                                                .coefficient(midterm.getCoefficient())
                                                .maxColumn(midterm.getMaxColumn())
                                                .build())
                                .finalExamConfig(ConfigDetailResponse.builder()
                                                .coefficient(finalExam.getCoefficient())
                                                .maxColumn(finalExam.getMaxColumn())
                                                .build())
                                .build();
        }

        // Cap nhat config grade
        @Transactional
        @Override
        public GradeConfigsResponse updateGradeConfigs(UpdateGradeConfigsRequest request) {
                Subject subject = subjectRepo.findById(request.subjectId())
                                .orElseThrow(() -> new ResourceNotFoundException("Môn học không tồn tại"));

                List<GradeConfig> gradeConfigs = gradeConfigRepo.findBySubjectId(subject.getId());
                if (gradeConfigs.isEmpty()) {
                        throw new ResourceNotFoundException("Môn học này chưa được thiết lập cấu hình điểm");
                }

                GradeConfig oral = gradeConfigs.stream().filter(c -> c.getExamType() == ExamType.MIENG).findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException("Cấu hình điểm miệng không tồn tại"));
                oral.setCoefficient(request.oralExamConfig().coefficient());
                oral.setMaxColumn(request.oralExamConfig().maxColumn());

                GradeConfig quiz = gradeConfigs.stream().filter(c -> c.getExamType() == ExamType.PHUT_15).findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Cấu hình điểm 15 phút không tồn tại"));
                quiz.setCoefficient(request.quizExamConfig().coefficient());
                quiz.setMaxColumn(request.quizExamConfig().maxColumn());

                GradeConfig midterm = gradeConfigs.stream().filter(c -> c.getExamType() == ExamType.TIET_1).findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Cấu hình điểm một tiết không tồn tại"));
                midterm.setCoefficient(request.midtermExamConfig().coefficient());
                midterm.setMaxColumn(request.midtermExamConfig().maxColumn());

                GradeConfig finalExam = gradeConfigs.stream().filter(c -> c.getExamType() == ExamType.HOC_KY)
                                .findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Cấu hình điểm cuối kỳ không tồn tại"));
                finalExam.setCoefficient(request.finalExamConfig().coefficient());
                finalExam.setMaxColumn(request.finalExamConfig().maxColumn());

                gradeConfigRepo.saveAll(List.of(oral, quiz, midterm, finalExam));

                return GradeConfigsResponse.builder()
                                .subjectId(subject.getId())
                                .subjectName(subject.getName())
                                .oralExamConfig(mapToDetail(oral))
                                .quizExamConfig(mapToDetail(quiz))
                                .midtermExamConfig(mapToDetail(midterm))
                                .finalExamConfig(mapToDetail(finalExam))
                                .build();
        }

        @Override
        public GradeConfigsResponse getGradeConfigsBySubjectId(Long subjectId) {
                Subject subject = subjectRepo.findById(subjectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Môn học không tồn tại"));
                List<GradeConfig> configs = gradeConfigRepo.findBySubjectId(subject.getId());

                if (configs.isEmpty()) {
                        throw new ResourceNotFoundException("Môn học này chưa được thiết lập cấu hình điểm");
                }

                GradeConfig oral = configs.stream().filter(c -> c.getExamType() == ExamType.MIENG).findFirst()
                                .orElse(null);
                GradeConfig quiz = configs.stream().filter(c -> c.getExamType() == ExamType.PHUT_15).findFirst()
                                .orElse(null);
                GradeConfig midterm = configs.stream().filter(c -> c.getExamType() == ExamType.TIET_1).findFirst()
                                .orElse(null);
                GradeConfig finalExam = configs.stream().filter(c -> c.getExamType() == ExamType.HOC_KY).findFirst()
                                .orElse(null);

                return GradeConfigsResponse.builder()
                                .subjectId(subject.getId())
                                .subjectName(subject.getName())
                                .oralExamConfig(mapToDetail(oral))
                                .quizExamConfig(mapToDetail(quiz))
                                .midtermExamConfig(mapToDetail(midterm))
                                .finalExamConfig(mapToDetail(finalExam))
                                .build();
        }

        private GradeConfigsResponse.ConfigDetailResponse mapToDetail(GradeConfig config) {
                if (config == null)
                        return null;
                return GradeConfigsResponse.ConfigDetailResponse.builder()
                                .coefficient(config.getCoefficient())
                                .maxColumn(config.getMaxColumn())
                                .build();
        }
}
