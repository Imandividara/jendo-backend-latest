package com.jendo.app.domain.reportattachment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportAttachmentResponseDto {
    private Long id;
    private String fileUrl;
    private String fileType;
    private LocalDateTime uploadedAt;
    private Long reportItemValueId;
    private String downloadUrl;
}
