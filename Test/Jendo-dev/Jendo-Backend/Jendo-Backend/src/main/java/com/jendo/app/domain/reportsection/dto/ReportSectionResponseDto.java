package com.jendo.app.domain.reportsection.dto;

import com.jendo.app.domain.reportitem.dto.ReportItemResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSectionResponseDto {
    private Long id;
    private String name;
    private String icon;
    private String description;
    private Long categoryId;
    private String categoryName;
    private List<ReportItemResponseDto> items;
}
