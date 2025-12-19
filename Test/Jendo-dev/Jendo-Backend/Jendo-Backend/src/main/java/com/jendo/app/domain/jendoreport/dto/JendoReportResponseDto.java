package com.jendo.app.domain.jendoreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JendoReportResponseDto {
    
    private Long id;
    private Long userId;
    private String fileName;
    private String originalFileName;
    private Long fileSize;
    private String contentType;
    private String description;
    private LocalDateTime uploadedAt;
    private String downloadUrl;
}
