package com.mth.academicfeesystem.common.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private String timestamp;
    private Map<String, Object> details;
    public ErrorResponse(int status, String error, String message, String path, String timestamp){
        this.status=status;
        this.error=error;
        this.message=message;
        this.path=path;
        this.timestamp=timestamp;
    }
}
