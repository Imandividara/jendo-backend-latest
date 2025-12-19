package com.jendo.app.domain.reportitemvalue.dto;

import com.jendo.app.domain.reportattachment.dto.ReportAttachmentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemValueResponseDto {
    private Long id;
    private Long reportItemId;
    private String reportItemName;
    private Long userId;
    private BigDecimal valueNumber;
    private String valueText;
    private LocalDate valueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ReportAttachmentResponseDto> attachments;
}
