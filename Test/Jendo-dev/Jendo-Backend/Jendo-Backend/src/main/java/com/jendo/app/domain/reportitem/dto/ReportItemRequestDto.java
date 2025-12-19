package com.jendo.app.domain.reportitem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemRequestDto {
    @NotBlank(message = "Item name is required")
    private String name;
    private String description;
    private String icon;
    private Long sectionId;
}
