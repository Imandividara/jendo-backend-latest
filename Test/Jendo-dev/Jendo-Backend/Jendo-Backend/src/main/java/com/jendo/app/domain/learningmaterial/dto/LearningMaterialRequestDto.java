package com.jendo.app.domain.learningmaterial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Learning material creation/update request")
public class LearningMaterialRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    @Schema(description = "Material title", example = "Understanding Heart Health", required = true)
    private String title;

    @Size(max = 200, message = "Author must not exceed 200 characters")
    @Schema(description = "Author name", example = "Dr. John Smith")
    private String author;

    @Size(max = 50, message = "Duration must not exceed 50 characters")
    @Schema(description = "Duration of content", example = "15 mins")
    private String duration;

    @Schema(description = "Material description", example = "A comprehensive guide to maintaining heart health")
    private String description;

    @Size(max = 50, message = "Type must not exceed 50 characters")
    @Schema(description = "Content type", example = "VIDEO")
    private String type;

    @Schema(description = "URL to video content", example = "https://example.com/video.mp4")
    private String videoUrl;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    @Schema(description = "Content category", example = "Cardiology")
    private String category;
}
