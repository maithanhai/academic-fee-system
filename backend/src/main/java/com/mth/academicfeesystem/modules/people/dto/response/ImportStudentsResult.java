package com.mth.academicfeesystem.modules.people.dto.response;

import java.util.List;

public record ImportStudentsResult(
        Long importedCount,
        List<String> skippedRows
) {
}
