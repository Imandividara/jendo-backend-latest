package com.jendo.app.domain.healthparameter.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.healthparameter.dto.HealthParameterRequestDto;
import com.jendo.app.domain.healthparameter.dto.HealthParameterResponseDto;

public interface HealthParameterService {
    
    HealthParameterResponseDto createHealthParameter(HealthParameterRequestDto request);
    
    HealthParameterResponseDto getHealthParameterById(Long id);
    
    PaginationResponse<HealthParameterResponseDto> getHealthParametersByUserId(Long userId, int page, int size);
    
    HealthParameterResponseDto getLatestHealthParameterByUserId(Long userId);
    
    HealthParameterResponseDto updateHealthParameter(Long id, HealthParameterRequestDto request);
    
    void deleteHealthParameter(Long id);
}
