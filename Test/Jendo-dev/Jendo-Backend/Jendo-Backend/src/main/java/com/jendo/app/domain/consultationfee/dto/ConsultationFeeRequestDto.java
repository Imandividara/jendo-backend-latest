package com.jendo.app.domain.consultationfee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Consultation fee creation/update request")
public class ConsultationFeeRequestDto {

    @Schema(description = "Fee type", example = "Initial Consultation")
    private String feeType;

    @Schema(description = "Fee amount", example = "150.00")
    private BigDecimal amount;

    @Schema(description = "Currency", example = "USD")
    private String currency;
}
