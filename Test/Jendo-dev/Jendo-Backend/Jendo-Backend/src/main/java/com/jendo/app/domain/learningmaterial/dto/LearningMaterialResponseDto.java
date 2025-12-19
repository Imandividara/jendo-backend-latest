package com.jendo.app.domain.learningmaterial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Learning material response data")
public class LearningMaterialResponseDto {

    @Schema(description = "Material unique identifier", example = "1")
    private Long id;

    @Schema(description = "Material title", example = "Understanding Heart Health")
    private String title;

    @Schema(description = "Author name", example = "Dr. John Smith")
    private String author;

    @Schema(description = "Duration of content", example = "15 mins")
    private String duration;

    @Schema(description = "Material description", example = "A comprehensive guide to maintaining heart health")
    private String description;

    @Schema(description = "Content type", example = "VIDEO")
    private String type;

    @Schema(description = "URL to video content", example = "https://example.com/video.mp4")
    private String videoUrl;

    @Schema(description = "Content category", example = "Cardiology")
    private String category;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
}
