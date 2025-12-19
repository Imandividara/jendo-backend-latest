package com.jendo.app.domain.learningmaterial.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialRequestDto;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialResponseDto;

public interface LearningMaterialService {
    
    LearningMaterialResponseDto createLearningMaterial(LearningMaterialRequestDto request);
    
    LearningMaterialResponseDto getLearningMaterialById(Long id);
    
    PaginationResponse<LearningMaterialResponseDto> getAllLearningMaterials(int page, int size);
    
    PaginationResponse<LearningMaterialResponseDto> getLearningMaterialsByCategory(String category, int page, int size);
    
    PaginationResponse<LearningMaterialResponseDto> searchLearningMaterials(String query, int page, int size);
    
    LearningMaterialResponseDto updateLearningMaterial(Long id, LearningMaterialRequestDto request);
    
    void deleteLearningMaterial(Long id);
}
