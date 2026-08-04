package com.mth.academicfeesystem.modules.grade.dto.request;

import com.mth.academicfeesystem.common.enums.ExamType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GradeConfigRequest {
    @NotNull(message = "ID môn học không được để trống")
    private Long subjectId;

    @NotNull(message = "Loại điểm không được để trống")
    private ExamType examType;

    @NotNull(message = "Hệ số không được để trống")
    @Min(value = 1, message = "Hệ số tối thiểu là 1")
    private Integer coefficient;

    @NotNull(message = "Số cột tối đa không được để trống")
    @Min(value = 1, message = "Cần ít nhất 1 cột điểm")
    private Integer maxColumn;
}
