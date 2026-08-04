package com.mth.academicfeesystem.modules.grade.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.grade.dto.request.GradeConfigRequest;
import com.mth.academicfeesystem.modules.grade.dto.response.GradeConfigDetailResponse;
import com.mth.academicfeesystem.modules.grade.dto.response.SubjectGradeConfigResponse;
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
    public void createConfig(GradeConfigRequest request) {
        Subject subject = subjectRepo.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));

        if (gradeConfigRepo.existsBySubjectIdAndExamType(subject.getId(), request.getExamType())) {
            throw new BusinessException("Môn học này đã có cấu hình cho loại điểm " + request.getExamType().name());
        }

        GradeConfig config = GradeConfig.builder()
                .subject(subject)
                .examType(request.getExamType())
                .coefficient(request.getCoefficient())
                .maxColumn(request.getMaxColumn())
                .build();

        gradeConfigRepo.save(config);
    }

    // Cap nhat config grade
    @Transactional
    @Override
    public void updateConfig(Long configId, GradeConfigRequest request) {
        GradeConfig existingConfig = gradeConfigRepo.findById(configId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu hình điểm"));

        existingConfig.setCoefficient(request.getCoefficient());
        existingConfig.setMaxColumn(request.getMaxColumn());

        gradeConfigRepo.save(existingConfig);
    }

    @Override
    public List<SubjectGradeConfigResponse> getAllConfigsGroupedBySubject() {
        List<GradeConfig> allConfigs = gradeConfigRepo.findAll();
        Map<Subject, List<GradeConfig>> groupedConfigs = allConfigs.stream()
                .collect(Collectors.groupingBy(GradeConfig::getSubject));

        return groupedConfigs.entrySet().stream()
                .map(entry -> {
                    Subject subject = entry.getKey();
                    List<GradeConfig> configs = entry.getValue();

                    List<GradeConfigDetailResponse> responses = configs.stream()
                            .map(config -> new GradeConfigDetailResponse(
                                    config.getId(),
                                    config.getExamType(),
                                    config.getCoefficient(),
                                    config.getMaxColumn()))
                            .toList();

                    return new SubjectGradeConfigResponse(
                            subject.getId(),
                            subject.getName(),
                            responses);
                })
                .toList();
    }
}
