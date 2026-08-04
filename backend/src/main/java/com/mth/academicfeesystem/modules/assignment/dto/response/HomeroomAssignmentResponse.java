package com.mth.academicfeesystem.modules.assignment.dto.response;

import java.time.LocalDate;

public record HomeroomAssignmentResponse (
    Long id,
    Long classId,
    String className,
    Long teacherId,
    String teacherName,
    LocalDate startDate,
    String status
){

}
