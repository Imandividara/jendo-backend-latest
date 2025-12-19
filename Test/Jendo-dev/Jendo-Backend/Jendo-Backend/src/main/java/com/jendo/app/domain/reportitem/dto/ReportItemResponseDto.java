package com.jendo.app.domain.reportitem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Long sectionId;
    private String sectionName;
}
