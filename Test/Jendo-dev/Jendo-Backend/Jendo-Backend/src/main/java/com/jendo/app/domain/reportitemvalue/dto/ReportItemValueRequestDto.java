package com.jendo.app.domain.reportitemvalue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemValueRequestDto {
    private Long reportItemId;
    private Long userId;
    private BigDecimal valueNumber;
    private String valueText;
    private LocalDate valueDate;
}
