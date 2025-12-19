package com.jendo.app.domain.reportsection.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSectionRequestDto {
    @NotBlank(message = "Section name is required")
    private String name;
    private String icon;
    private String description;
    private Long categoryId;
}
