package com.jendo.app.domain.healthparameter.mapper;

import com.jendo.app.domain.healthparameter.dto.HealthParameterRequestDto;
import com.jendo.app.domain.healthparameter.dto.HealthParameterResponseDto;
import com.jendo.app.domain.healthparameter.entity.HealthParameter;
import com.jendo.app.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HealthParameterMapper {

    public HealthParameter toEntity(HealthParameterRequestDto dto, User user) {
        return HealthParameter.builder()
                .user(user)
                .bloodType(dto.getBloodType())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .bmi(dto.getBmi())
                .build();
    }

    public HealthParameterResponseDto toResponseDto(HealthParameter entity) {
        return HealthParameterResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .bloodType(entity.getBloodType())
                .height(entity.getHeight())
                .weight(entity.getWeight())
                .bmi(entity.getBmi())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
