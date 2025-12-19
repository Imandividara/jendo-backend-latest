package com.jendo.app.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Validation error detail")
public class ErrorDetail {
    
    @Schema(description = "Field that failed validation", example = "email")
    private String field;
    
    @Schema(description = "Error message", example = "must be a valid email address")
    private String message;
}
