package com.mth.academicfeesystem.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getter,setter
@Builder //builder
@NoArgsConstructor  //constructor khong tham so
@AllArgsConstructor //constructor full tham so
@JsonInclude(JsonInclude.Include.NON_NULL)  // loai bo null
public class ApiResponse<T> {
    private String message;
    private T data;

    public ApiResponse(String message) {
        this.message = message;
    }
}
