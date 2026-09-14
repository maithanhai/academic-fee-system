package com.mth.academicfeesystem.modules.grade.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mth.academicfeesystem.common.enums.ExamType;
import com.mth.academicfeesystem.modules.grade.entity.GradeConfig;

public interface GradeConfigRepository extends JpaRepository<GradeConfig, Long> {
    List<GradeConfig> findBySubjectId(Long subjectId);

    boolean existsBySubjectIdAndExamType(Long subjectId, ExamType examType);

    Optional<GradeConfig> findBySubjectIdAndExamType(Long subjectId, ExamType examType);

}
