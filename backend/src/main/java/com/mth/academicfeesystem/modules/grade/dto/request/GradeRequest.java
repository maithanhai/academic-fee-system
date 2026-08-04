package com.mth.academicfeesystem.modules.grade.dto.request;

import java.util.List;

import com.mth.academicfeesystem.common.enums.ExamType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GradeRequest(
    @NotNull(message = "ID Lớp học không được để trống")
    Long classId,

    @NotNull(message = "ID Môn học không được để trống")
    Long subjectId,

    @NotNull(message = "ID Học kỳ không được để trống")
    Long semesterId,

    @NotNull(message = "Loại điểm không được để trống")
    ExamType examType,

    @NotNull(message = "Số thứ tự cột điểm không được để trống")
    @Min(value = 1, message = "Cột điểm phải bắt đầu từ 1")
    Integer ordinalNumber,

    @NotEmpty(message = "Danh sách điểm không được để trống")
    @Valid // Kích hoạt validate cho các phần tử con bên trong list
    List<StudentScoreRequest> scores
) {
    public record StudentScoreRequest(
        @NotNull(message = "ID Học sinh không được để trống")
        Long studentId,

        @NotNull(message = "Điểm số không được để trống")
        @Min(value = 0, message = "Điểm không được nhỏ hơn 0")
        @Max(value = 10, message = "Điểm không được lớn hơn 10")
        Double scoreValue
    ) {}
} 