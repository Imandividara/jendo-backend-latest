package com.jendo.app.domain.wellnessrecommendation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WellnessRecommendationRequestDto {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String category;
    
    @NotBlank(message = "Risk level is required")
    private String riskLevel;
    
    private String type;
    
    private Integer priority;
    
    private Boolean isActive;
}
