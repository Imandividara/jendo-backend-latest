package com.jendo.app.domain.learningmaterial.mapper;

import com.jendo.app.domain.learningmaterial.dto.LearningMaterialRequestDto;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialResponseDto;
import com.jendo.app.domain.learningmaterial.entity.LearningMaterial;
import org.springframework.stereotype.Component;

@Component
public class LearningMaterialMapper {

    public LearningMaterial toEntity(LearningMaterialRequestDto dto) {
        return LearningMaterial.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .duration(dto.getDuration())
                .description(dto.getDescription())
                .type(dto.getType())
                .videoUrl(dto.getVideoUrl())
                .category(dto.getCategory())
                .build();
    }

    public LearningMaterialResponseDto toResponseDto(LearningMaterial entity) {
        return LearningMaterialResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .duration(entity.getDuration())
                .description(entity.getDescription())
                .type(entity.getType())
                .videoUrl(entity.getVideoUrl())
                .category(entity.getCategory())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
