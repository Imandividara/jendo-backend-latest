package com.jendo.app.domain.reportcategory.dto;

import com.jendo.app.domain.reportsection.dto.ReportSectionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCategoryResponseDto {
    private Long id;
    private String name;
    private String icon;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private List<ReportSectionResponseDto> sections;
}
