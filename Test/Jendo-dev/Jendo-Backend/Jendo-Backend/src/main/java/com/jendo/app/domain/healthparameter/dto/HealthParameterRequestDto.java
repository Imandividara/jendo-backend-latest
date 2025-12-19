package com.jendo.app.domain.healthparameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Health parameter creation/update request")
public class HealthParameterRequestDto {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    @Schema(description = "ID of the user", example = "1", required = true)
    private Long userId;

    @Schema(description = "Blood type", example = "O+")
    private String bloodType;

    @Positive(message = "Height must be positive")
    @Schema(description = "Height in cm", example = "175.5")
    private BigDecimal height;

    @Positive(message = "Weight must be positive")
    @Schema(description = "Weight in kg", example = "70.5")
    private BigDecimal weight;

    @Schema(description = "Body Mass Index", example = "22.9")
    private BigDecimal bmi;
}
