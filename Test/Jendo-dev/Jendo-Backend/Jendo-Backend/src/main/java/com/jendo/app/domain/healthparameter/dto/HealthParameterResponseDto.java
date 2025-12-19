package com.jendo.app.domain.healthparameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Health parameter response data")
public class HealthParameterResponseDto {

    @Schema(description = "Parameter unique identifier", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Blood type", example = "O+")
    private String bloodType;

    @Schema(description = "Height in cm", example = "175.5")
    private BigDecimal height;

    @Schema(description = "Weight in kg", example = "70.5")
    private BigDecimal weight;

    @Schema(description = "Body Mass Index", example = "22.9")
    private BigDecimal bmi;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
